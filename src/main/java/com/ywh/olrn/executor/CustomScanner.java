package com.ywh.olrn.executor;

import sun.misc.LRUCache;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 Scanner 类：
 * 修改构造方法，其他几个方法返回类型修改为 CustomScanner
 */
public final class CustomScanner implements Iterator<String>, Closeable {

    private CharBuffer buf;

    private static final int BUFFER_SIZE = 1024;

    private int position;

    private Matcher matcher;

    private Pattern delimPattern;

    private Pattern hasNextPattern;

    private int hasNextPosition;

    private String hasNextResult;

    private Readable source;

    private boolean sourceClosed = false;

    private boolean needInput = false;

    private boolean skipped = false;

    private int savedScannerPosition = -1;

    private Object typeCache = null;

    private boolean matchValid = false;

    private boolean closed = false;

    private int radix = 10;

    private int defaultRadix = 10;

    private Locale locale = null;

    private LRUCache<String, Pattern> patternCache =
        new LRUCache<String, Pattern>(7) {
            @Override
            protected Pattern create(String s) {
                return Pattern.compile(s);
            }

            @Override
            protected boolean hasName(Pattern p, String s) {
                return p.pattern().equals(s);
            }
        };

    private IOException lastException;

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");

    private static final Pattern FIND_ANY_PATTERN = Pattern.compile("(?s).*");

    private static final Pattern NON_ASCII_DIGIT = Pattern.compile(
        "[\\p{javaDigit}&&[^0-9]]");

    private String groupSeparator = "\\,";
    private String decimalSeparator = "\\.";
    private String nanString = "NaN";
    private String infinityString = "Infinity";
    private String positivePrefix = "";
    private String negativePrefix = "\\-";
    private String positiveSuffix = "";
    private String negativeSuffix = "";

    private static volatile Pattern boolPattern;

    private static final String BOOLEAN_PATTERN = "true|false";

    private static Pattern boolPattern() {
        Pattern bp = boolPattern;
        if (bp == null) {
            boolPattern = bp = Pattern.compile(BOOLEAN_PATTERN,
                Pattern.CASE_INSENSITIVE);
        }
        return bp;
    }

    private Pattern integerPattern;
    private String digits = "0123456789abcdefghijklmnopqrstuvwxyz";
    private String non0Digit = "[\\p{javaDigit}&&[^0]]";
    private int SIMPLE_GROUP_INDEX = 5;

    private String buildIntegerPatternString() {
        String radixDigits = digits.substring(0, radix);
        String digit = "((?i)[" + radixDigits + "]|\\p{javaDigit})";
        String groupedNumeral = "(" + non0Digit + digit + "?" + digit + "?(" +
            groupSeparator + digit + digit + digit + ")+)";
        String numeral = "((" + digit + "++)|" + groupedNumeral + ")";
        String javaStyleInteger = "([-+]?(" + numeral + "))";
        String negativeInteger = negativePrefix + numeral + negativeSuffix;
        String positiveInteger = positivePrefix + numeral + positiveSuffix;
        return "(" + javaStyleInteger + ")|(" +
            positiveInteger + ")|(" +
            negativeInteger + ")";
    }

    private Pattern integerPattern() {
        if (integerPattern == null) {
            integerPattern = patternCache.forName(buildIntegerPatternString());
        }
        return integerPattern;
    }

    private static volatile Pattern separatorPattern;
    private static volatile Pattern linePattern;
    private static final String LINE_SEPARATOR_PATTERN =
        "\r\n|[\n\r\u2028\u2029\u0085]";
    private static final String LINE_PATTERN = ".*(" + LINE_SEPARATOR_PATTERN + ")|.+$";

    private static Pattern separatorPattern() {
        Pattern sp = separatorPattern;
        if (sp == null) {
            separatorPattern = sp = Pattern.compile(LINE_SEPARATOR_PATTERN);
        }
        return sp;
    }

    private static Pattern linePattern() {
        Pattern lp = linePattern;
        if (lp == null) {
            linePattern = lp = Pattern.compile(LINE_PATTERN);
        }
        return lp;
    }

    private Pattern floatPattern;
    private Pattern decimalPattern;

    private void buildFloatAndDecimalPattern() {
        String digit = "([0-9]|(\\p{javaDigit}))";
        String exponent = "([eE][+-]?" + digit + "+)?";
        String groupedNumeral = "(" + non0Digit + digit + "?" + digit + "?(" + groupSeparator + digit + digit + digit + ")+)";
        String numeral = "((" + digit + "++)|" + groupedNumeral + ")";
        String decimalNumeral = "(" + numeral + "|" + numeral + decimalSeparator + digit + "*+|" + decimalSeparator + digit + "++)";
        String nonNumber = "(NaN|" + nanString + "|Infinity|" + infinityString + ")";
        String positiveFloat = "(" + positivePrefix + decimalNumeral + positiveSuffix + exponent + ")";
        String negativeFloat = "(" + negativePrefix + decimalNumeral + negativeSuffix + exponent + ")";
        String decimal = "(([-+]?" + decimalNumeral + exponent + ")|" + positiveFloat + "|" + negativeFloat + ")";
        String hexFloat = "[-+]?0[xX][0-9a-fA-F]*\\.[0-9a-fA-F]+([pP][-+]?[0-9]+)?";
        String positiveNonNumber = "(" + positivePrefix + nonNumber + positiveSuffix + ")";
        String negativeNonNumber = "(" + negativePrefix + nonNumber + negativeSuffix + ")";
        String signedNonNumber = "(([-+]?" + nonNumber + ")|" + positiveNonNumber + "|" + negativeNonNumber + ")";
        floatPattern = Pattern.compile(decimal + "|" + hexFloat + "|" + signedNonNumber);
        decimalPattern = Pattern.compile(decimal);
    }

    private Pattern floatPattern() {
        if (floatPattern == null) {
            buildFloatAndDecimalPattern();
        }
        return floatPattern;
    }

    private Pattern decimalPattern() {
        if (decimalPattern == null) {
            buildFloatAndDecimalPattern();
        }
        return decimalPattern;
    }

    /**
     *
     * @param source
     * @param pattern
     */
    private CustomScanner(Readable source, Pattern pattern) {
        assert source != null : "source should not be null";
        assert pattern != null : "pattern should not be null";
        this.source = source;
        delimPattern = pattern;
        buf = CharBuffer.allocate(BUFFER_SIZE);
        buf.limit(0);
        matcher = delimPattern.matcher(buf);
        matcher.useTransparentBounds(true);
        matcher.useAnchoringBounds(false);
        useLocale(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     *
     * @param source
     */
    public CustomScanner(Readable source) {
        this(Objects.requireNonNull(source, "source"), WHITESPACE_PATTERN);
    }

    /**
     *
     * @param source
     */
    public CustomScanner(InputStream source) {
        // 判断一下输入的 InputStream 是不是 CustomInputStream，
        // 如果是就调用一下它的 get 方法，把当前线程的输入流从 ThreadLocal 中取出来传入
        this(new InputStreamReader(
                (source instanceof CustomInputStream) ? ((CustomInputStream) source).get() : source),
            WHITESPACE_PATTERN);
    }

    /**
     *
     * @param source
     * @param charsetName
     */
    public CustomScanner(InputStream source, String charsetName) {
        this(makeReadable(Objects.requireNonNull(source, "source"), toCharset(charsetName)),
            WHITESPACE_PATTERN);
    }

    /**
     *
     * @param csn
     * @return
     */
    private static Charset toCharset(String csn) {
        Objects.requireNonNull(csn, "charsetName");
        try {
            return Charset.forName(csn);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            // IllegalArgumentException should be thrown
            throw new IllegalArgumentException(e);
        }
    }

    private static Readable makeReadable(InputStream source, Charset charset) {
        return new InputStreamReader(source, charset);
    }

    /**
     *
     * @param source
     * @throws FileNotFoundException
     */
    public CustomScanner(File source) throws FileNotFoundException {
        this((ReadableByteChannel) (new FileInputStream(source).getChannel()));
    }

    /**
     *
     * @param source
     * @param charsetName
     * @throws FileNotFoundException
     */
    public CustomScanner(File source, String charsetName)
        throws FileNotFoundException {
        this(Objects.requireNonNull(source), toDecoder(charsetName));
    }

    private CustomScanner(File source, CharsetDecoder dec)
        throws FileNotFoundException {
        this(makeReadable((ReadableByteChannel) (new FileInputStream(source).getChannel()), dec));
    }

    private static CharsetDecoder toDecoder(String charsetName) {
        Objects.requireNonNull(charsetName, "charsetName");
        try {
            return Charset.forName(charsetName).newDecoder();
        } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
            throw new IllegalArgumentException(charsetName);
        }
    }

    private static Readable makeReadable(ReadableByteChannel source,
                                         CharsetDecoder dec) {
        return Channels.newReader(source, dec, -1);
    }

    /**
     *
     * @param source
     * @throws IOException
     */
    public CustomScanner(Path source)
        throws IOException {
        this(Files.newInputStream(source));
    }

    /**
     *
     * @param source
     * @param charsetName
     * @throws IOException
     */
    public CustomScanner(Path source, String charsetName) throws IOException {
        this(Objects.requireNonNull(source), toCharset(charsetName));
    }

    private CustomScanner(Path source, Charset charset) throws IOException {
        this(makeReadable(Files.newInputStream(source), charset));
    }

    /**
     *
     * @param source
     */
    public CustomScanner(String source) {
        this(new StringReader(source), WHITESPACE_PATTERN);
    }

    /**
     *
     * @param source
     */
    public CustomScanner(ReadableByteChannel source) {
        this(makeReadable(Objects.requireNonNull(source, "source")),
            WHITESPACE_PATTERN);
    }

    private static Readable makeReadable(ReadableByteChannel source) {
        return makeReadable(source, Charset.defaultCharset().newDecoder());
    }

    /**
     *
     * @param source
     * @param charsetName
     */
    public CustomScanner(ReadableByteChannel source, String charsetName) {
        this(makeReadable(Objects.requireNonNull(source, "source"), toDecoder(charsetName)),
            WHITESPACE_PATTERN);
    }

    private void saveState() {
        savedScannerPosition = position;
    }

    private void revertState() {
        this.position = savedScannerPosition;
        savedScannerPosition = -1;
        skipped = false;
    }

    private boolean revertState(boolean b) {
        this.position = savedScannerPosition;
        savedScannerPosition = -1;
        skipped = false;
        return b;
    }

    private void cacheResult() {
        hasNextResult = matcher.group();
        hasNextPosition = matcher.end();
        hasNextPattern = matcher.pattern();
    }

    private void cacheResult(String result) {
        hasNextResult = result;
        hasNextPosition = matcher.end();
        hasNextPattern = matcher.pattern();
    }

    // Clears both regular cache and type cache
    private void clearCaches() {
        hasNextPattern = null;
        typeCache = null;
    }

    // Also clears both the regular cache and the type cache
    private String getCachedResult() {
        position = hasNextPosition;
        hasNextPattern = null;
        typeCache = null;
        return hasNextResult;
    }

    // Also clears both the regular cache and the type cache
    private void useTypeCache() {
        if (closed) {
            throw new IllegalStateException("Scanner closed");
        }
        position = hasNextPosition;
        hasNextPattern = null;
        typeCache = null;
    }

    // Tries to read more input. May block.
    private void readInput() {
        if (buf.limit() == buf.capacity()) {
            makeSpace();
        }

        // Prepare to receive data
        int p = buf.position();
        buf.position(buf.limit());
        buf.limit(buf.capacity());

        int n = 0;
        try {
            n = source.read(buf);
        } catch (IOException ioe) {
            lastException = ioe;
            n = -1;
        }

        if (n == -1) {
            sourceClosed = true;
            needInput = false;
        }

        if (n > 0) {
            needInput = false;
        }

        // Restore current position and limit for reading
        buf.limit(buf.position());
        buf.position(p);
    }
    private boolean makeSpace() {
        clearCaches();
        int offset = savedScannerPosition == -1 ?
            position : savedScannerPosition;
        buf.position(offset);
        // Gain space by compacting buffer
        if (offset > 0) {
            buf.compact();
            translateSavedIndexes(offset);
            position -= offset;
            buf.flip();
            return true;
        }
        // Gain space by growing buffer
        int newSize = buf.capacity() * 2;
        CharBuffer newBuf = CharBuffer.allocate(newSize);
        newBuf.put(buf);
        newBuf.flip();
        translateSavedIndexes(offset);
        position -= offset;
        buf = newBuf;
        matcher.reset(buf);
        return true;
    }
    private void translateSavedIndexes(int offset) {
        if (savedScannerPosition != -1) {
            savedScannerPosition -= offset;
        }
    }
    private void throwFor() {
        skipped = false;
        if ((sourceClosed) && (position == buf.limit())) {
            throw new NoSuchElementException();
        } else {
            throw new InputMismatchException();
        }
    }
    private boolean hasTokenInBuffer() {
        matchValid = false;
        matcher.usePattern(delimPattern);
        matcher.region(position, buf.limit());

        // Skip delims first
        if (matcher.lookingAt()) {
            position = matcher.end();
        }

        // If we are sitting at the end, no more tokens in buffer
        if (position == buf.limit())
            return false;

        return true;
    }

    /**
     *
     * @param pattern
     * @return
     */
    private String getCompleteTokenInBuffer(Pattern pattern) {
        matchValid = false;

        // Skip delims first
        matcher.usePattern(delimPattern);
        if (!skipped) { // Enforcing only one skip of leading delims
            matcher.region(position, buf.limit());
            if (matcher.lookingAt()) {
                // If more input could extend the delimiters then we must wait
                // for more input
                if (matcher.hitEnd() && !sourceClosed) {
                    needInput = true;
                    return null;
                }
                // The delims were whole and the matcher should skip them
                skipped = true;
                position = matcher.end();
            }
        }

        // If we are sitting at the end, no more tokens in buffer
        if (position == buf.limit()) {
            if (sourceClosed) {
                return null;
            }
            needInput = true;
            return null;
        }
        matcher.region(position, buf.limit());
        boolean foundNextDelim = matcher.find();
        if (foundNextDelim && (matcher.end() == position)) {
            foundNextDelim = matcher.find();
        }
        if (foundNextDelim) {
            if (matcher.requireEnd() && !sourceClosed) {
                needInput = true;
                return null;
            }
            int tokenEnd = matcher.start();
            // There is a complete token.
            if (pattern == null) {
                pattern = FIND_ANY_PATTERN;
            }
            matcher.usePattern(pattern);
            matcher.region(position, tokenEnd);
            if (matcher.matches()) {
                String s = matcher.group();
                position = matcher.end();
                return s;
            } else { // Complete token but it does not match
                return null;
            }
        }

        if (sourceClosed) {
            if (pattern == null) {
                pattern = FIND_ANY_PATTERN;
            }
            matcher.usePattern(pattern);
            matcher.region(position, buf.limit());
            if (matcher.matches()) {
                String s = matcher.group();
                position = matcher.end();
                return s;
            }
            // Last piece does not match
            return null;
        }

        // There is a partial token in the buffer; must read more
        // to complete it
        needInput = true;
        return null;
    }

    private String findPatternInBuffer(Pattern pattern, int horizon) {
        matchValid = false;
        matcher.usePattern(pattern);
        int bufferLimit = buf.limit();
        int horizonLimit = -1;
        int searchLimit = bufferLimit;
        if (horizon > 0) {
            horizonLimit = position + horizon;
            if (horizonLimit < bufferLimit) {
                searchLimit = horizonLimit;
            }
        }
        matcher.region(position, searchLimit);
        if (matcher.find()) {
            if (matcher.hitEnd() && (!sourceClosed)) {
                // The match may be longer if didn't hit horizon or real end
                if (searchLimit != horizonLimit) {
                    // Hit an artificial end; try to extend the match
                    needInput = true;
                    return null;
                }
                if ((searchLimit == horizonLimit) && matcher.requireEnd()) {
                    needInput = true;
                    return null;
                }
            }
            // Did not hit end, or hit real end, or hit horizon
            position = matcher.end();
            return matcher.group();
        }

        if (sourceClosed) {
            return null;
        }

        // If there is no specified horizon, or if we have not searched
        // to the specified horizon yet, get more input
        if ((horizon == 0) || (searchLimit != horizonLimit)) {
            needInput = true;
        }
        return null;
    }

    private String matchPatternInBuffer(Pattern pattern) {
        matchValid = false;
        matcher.usePattern(pattern);
        matcher.region(position, buf.limit());
        if (matcher.lookingAt()) {
            if (matcher.hitEnd() && (!sourceClosed)) {
                // Get more input and try again
                needInput = true;
                return null;
            }
            position = matcher.end();
            return matcher.group();
        }

        if (sourceClosed) {
            return null;
        }

        // Read more to find pattern
        needInput = true;
        return null;
    }

    // Throws if the scanner is closed
    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Scanner closed");
        }
    }

    /**
     *
     */
    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (source instanceof Closeable) {
            try {
                ((Closeable) source).close();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        sourceClosed = true;
        source = null;
        closed = true;
    }

    /**
     *
     * @return
     */
    public IOException ioException() {
        return lastException;
    }

    /**
     * @return
     */
    public Pattern delimiter() {
        return delimPattern;
    }

    /**
     * @param pattern
     * @return
     */
    public CustomScanner useDelimiter(Pattern pattern) {
        delimPattern = pattern;
        return this;
    }

    /**
     * @param pattern
     * @return
     */
    public CustomScanner useDelimiter(String pattern) {
        delimPattern = patternCache.forName(pattern);
        return this;
    }

    /**
     * @return
     */
    public Locale locale() {
        return this.locale;
    }

    /**
     * @param locale
     * @return
     */
    public CustomScanner useLocale(Locale locale) {
        if (locale.equals(this.locale)) {
            return this;
        }

        this.locale = locale;
        DecimalFormat df =
            (DecimalFormat) NumberFormat.getNumberInstance(locale);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);

        // These must be literalized to avoid collision with regex
        // metacharacters such as dot or parenthesis
        groupSeparator = "\\" + dfs.getGroupingSeparator();
        decimalSeparator = "\\" + dfs.getDecimalSeparator();

        // Quoting the nonzero length locale-specific things
        // to avoid potential conflict with metacharacters
        nanString = "\\Q" + dfs.getNaN() + "\\E";
        infinityString = "\\Q" + dfs.getInfinity() + "\\E";
        positivePrefix = df.getPositivePrefix();
        if (positivePrefix.length() > 0) {
            positivePrefix = "\\Q" + positivePrefix + "\\E";
        }
        negativePrefix = df.getNegativePrefix();
        if (negativePrefix.length() > 0) {
            negativePrefix = "\\Q" + negativePrefix + "\\E";
        }
        positiveSuffix = df.getPositiveSuffix();
        if (positiveSuffix.length() > 0) {
            positiveSuffix = "\\Q" + positiveSuffix + "\\E";
        }
        negativeSuffix = df.getNegativeSuffix();
        if (negativeSuffix.length() > 0) {
            negativeSuffix = "\\Q" + negativeSuffix + "\\E";
        }

        // Force rebuilding and recompilation of locale dependent
        // primitive patterns
        integerPattern = null;
        floatPattern = null;

        return this;
    }

    /**
     * @return
     */
    public int radix() {
        return this.defaultRadix;
    }

    /**
     * @param radix
     * @return
     */
    public CustomScanner useRadix(int radix) {
        if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
            throw new IllegalArgumentException("radix:" + radix);
        }

        if (this.defaultRadix == radix) {
            return this;
        }
        this.defaultRadix = radix;
        // Force rebuilding and recompilation of radix dependent patterns
        integerPattern = null;
        return this;
    }

    // The next operation should occur in the specified radix but
    // the default is left untouched.
    private void setRadix(int radix) {
        if (this.radix != radix) {
            // Force rebuilding and recompilation of radix dependent patterns
            integerPattern = null;
            this.radix = radix;
        }
    }

    /**
     * @return
     */
    public MatchResult match() {
        if (!matchValid) {
            throw new IllegalStateException("No match result available");
        }
        return matcher.toMatchResult();
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("java.util.Scanner");
        sb.append("[delimiters=" + delimPattern + "]");
        sb.append("[position=" + position + "]");
        sb.append("[match valid=" + matchValid + "]");
        sb.append("[need input=" + needInput + "]");
        sb.append("[source closed=" + sourceClosed + "]");
        sb.append("[skipped=" + skipped + "]");
        sb.append("[group separator=" + groupSeparator + "]");
        sb.append("[decimal separator=" + decimalSeparator + "]");
        sb.append("[positive prefix=" + positivePrefix + "]");
        sb.append("[negative prefix=" + negativePrefix + "]");
        sb.append("[positive suffix=" + positiveSuffix + "]");
        sb.append("[negative suffix=" + negativeSuffix + "]");
        sb.append("[NaN string=" + nanString + "]");
        sb.append("[infinity string=" + infinityString + "]");
        return sb.toString();
    }

    /**
     * @return
     */
    @Override
    public boolean hasNext() {
        ensureOpen();
        saveState();
        while (!sourceClosed) {
            if (hasTokenInBuffer()) {
                return revertState(true);
            }
            readInput();
        }
        boolean result = hasTokenInBuffer();
        return revertState(result);
    }

    /**
     * @return
     */
    @Override
    public String next() {
        ensureOpen();
        clearCaches();

        while (true) {
            String token = getCompleteTokenInBuffer(null);
            if (token != null) {
                matchValid = true;
                skipped = false;
                return token;
            }
            if (needInput) {
                readInput();
            } else {
                throwFor();
            }
        }
    }

    /**
     *
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param pattern
     * @return
     */
    public boolean hasNext(String pattern) {
        return hasNext(patternCache.forName(pattern));
    }

    /**
     * @param pattern
     * @return
     */
    public String next(String pattern) {
        return next(patternCache.forName(pattern));
    }

    /**
     * @param pattern
     * @return
     */
    public boolean hasNext(Pattern pattern) {
        ensureOpen();
        if (pattern == null) {
            throw new NullPointerException();
        }
        hasNextPattern = null;
        saveState();

        while (true) {
            if (getCompleteTokenInBuffer(pattern) != null) {
                matchValid = true;
                cacheResult();
                return revertState(true);
            }
            if (needInput) {
                readInput();
            } else {
                return revertState(false);
            }
        }
    }

    /**
     * @param pattern
     * @return
     */
    public String next(Pattern pattern) {
        ensureOpen();
        if (pattern == null) {
            throw new NullPointerException();
        }

        // Did we already find this pattern?
        if (hasNextPattern == pattern) {
            return getCachedResult();
        }
        clearCaches();

        // Search for the pattern
        while (true) {
            String token = getCompleteTokenInBuffer(pattern);
            if (token != null) {
                matchValid = true;
                skipped = false;
                return token;
            }
            if (needInput) {
                readInput();
            } else {
                throwFor();
            }
        }
    }

    /**
     * @return
     */
    public boolean hasNextLine() {
        saveState();

        String result = findWithinHorizon(linePattern(), 0);
        if (result != null) {
            MatchResult mr = this.match();
            String lineSep = mr.group(1);
            if (lineSep != null) {
                result = result.substring(0, result.length() -
                    lineSep.length());
                cacheResult(result);

            } else {
                cacheResult();
            }
        }
        revertState();
        return (result != null);
    }

    /**
     * @return
     */
    public String nextLine() {
        if (hasNextPattern == linePattern()) {
            return getCachedResult();
        }
        clearCaches();

        String result = findWithinHorizon(linePattern, 0);
        if (result == null) {
            throw new NoSuchElementException("No line found");
        }
        MatchResult mr = this.match();
        String lineSep = mr.group(1);
        if (lineSep != null) {
            result = result.substring(0, result.length() - lineSep.length());
        }
        if (result == null) {
            throw new NoSuchElementException();
        } else {
            return result;
        }
    }

    /**
     * @param pattern
     * @return
     */
    public String findInLine(String pattern) {
        return findInLine(patternCache.forName(pattern));
    }

    /**
     * @param pattern
     * @return
     */
    public String findInLine(Pattern pattern) {
        ensureOpen();
        if (pattern == null) {
            throw new NullPointerException();
        }
        clearCaches();
        // Expand buffer to include the next newline or end of input
        int endPosition = 0;
        saveState();
        while (true) {
            String token = findPatternInBuffer(separatorPattern(), 0);
            if (token != null) {
                endPosition = matcher.start();
                break; // up to next newline
            }
            if (needInput) {
                readInput();
            } else {
                endPosition = buf.limit();
                break; // up to end of input
            }
        }
        revertState();
        int horizonForLine = endPosition - position;
        if (horizonForLine == 0) {
            return null;
        }
        // Search for the pattern
        return findWithinHorizon(pattern, horizonForLine);
    }

    /**
     * @param pattern
     * @param horizon
     * @return
     */
    public String findWithinHorizon(String pattern, int horizon) {
        return findWithinHorizon(patternCache.forName(pattern), horizon);
    }

    /**
     * @param pattern
     * @param horizon
     * @return
     */
    public String findWithinHorizon(Pattern pattern, int horizon) {
        ensureOpen();
        if (pattern == null) {
            throw new NullPointerException();
        }
        if (horizon < 0) {
            throw new IllegalArgumentException("horizon < 0");
        }
        clearCaches();

        // Search for the pattern
        while (true) {
            String token = findPatternInBuffer(pattern, horizon);
            if (token != null) {
                matchValid = true;
                return token;
            }
            if (needInput) {
                readInput();
            } else {
                break; // up to end of input
            }
        }
        return null;
    }

    /**
     * @param pattern
     * @return
     */
    public CustomScanner skip(Pattern pattern) {
        ensureOpen();
        if (pattern == null) {
            throw new NullPointerException();
        }
        clearCaches();

        // Search for the pattern
        while (true) {
            String token = matchPatternInBuffer(pattern);
            if (token != null) {
                matchValid = true;
                position = matcher.end();
                return this;
            }
            if (needInput) {
                readInput();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * @param pattern
     * @return
     */
    public CustomScanner skip(String pattern) {
        return skip(patternCache.forName(pattern));
    }

    /**
     * @return
     */
    public boolean hasNextBoolean() {
        return hasNext(boolPattern());
    }

    /**
     * @return
     */
    public boolean nextBoolean() {
        clearCaches();
        return Boolean.parseBoolean(next(boolPattern()));
    }

    /**
     * @return
     */
    public boolean hasNextByte() {
        return hasNextByte(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public boolean hasNextByte(int radix) {
        setRadix(radix);
        boolean result = hasNext(integerPattern());
        if (result) { // Cache it
            try {
                String s = (matcher.group(SIMPLE_GROUP_INDEX) == null) ?
                    processIntegerToken(hasNextResult) :
                    hasNextResult;
                typeCache = Byte.parseByte(s, radix);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public byte nextByte() {
        return nextByte(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public byte nextByte(int radix) {
        // Check cached result
        if ((typeCache != null) && (typeCache instanceof Byte)
            && this.radix == radix) {
            byte val = ((Byte) typeCache).byteValue();
            useTypeCache();
            return val;
        }
        setRadix(radix);
        clearCaches();
        // Search for next byte
        try {
            String s = next(integerPattern());
            if (matcher.group(SIMPLE_GROUP_INDEX) == null) {
                s = processIntegerToken(s);
            }
            return Byte.parseByte(s, radix);
        } catch (NumberFormatException nfe) {
            position = matcher.start(); // don't skip bad token
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean hasNextShort() {
        return hasNextShort(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public boolean hasNextShort(int radix) {
        setRadix(radix);
        boolean result = hasNext(integerPattern());
        if (result) { // Cache it
            try {
                String s = (matcher.group(SIMPLE_GROUP_INDEX) == null) ?
                    processIntegerToken(hasNextResult) :
                    hasNextResult;
                typeCache = Short.parseShort(s, radix);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public short nextShort() {
        return nextShort(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public short nextShort(int radix) {
        // Check cached result
        if ((typeCache != null) && (typeCache instanceof Short)
            && this.radix == radix) {
            short val = ((Short) typeCache).shortValue();
            useTypeCache();
            return val;
        }
        setRadix(radix);
        clearCaches();
        // Search for next short
        try {
            String s = next(integerPattern());
            if (matcher.group(SIMPLE_GROUP_INDEX) == null) {
                s = processIntegerToken(s);
            }
            return Short.parseShort(s, radix);
        } catch (NumberFormatException nfe) {
            position = matcher.start(); // don't skip bad token
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean hasNextInt() {
        return hasNextInt(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public boolean hasNextInt(int radix) {
        setRadix(radix);
        boolean result = hasNext(integerPattern());
        if (result) { // Cache it
            try {
                String s = (matcher.group(SIMPLE_GROUP_INDEX) == null) ?
                    processIntegerToken(hasNextResult) :
                    hasNextResult;
                typeCache = Integer.parseInt(s, radix);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @param token
     * @return
     */
    private String processIntegerToken(String token) {
        String result = token.replaceAll("" + groupSeparator, "");
        boolean isNegative = false;
        int preLen = negativePrefix.length();
        if ((preLen > 0) && result.startsWith(negativePrefix)) {
            isNegative = true;
            result = result.substring(preLen);
        }
        int sufLen = negativeSuffix.length();
        if ((sufLen > 0) && result.endsWith(negativeSuffix)) {
            isNegative = true;
            result = result.substring(result.length() - sufLen,
                result.length());
        }
        if (isNegative) {
            result = "-" + result;
        }
        return result;
    }

    /**
     * @return
     */
    public int nextInt() {
        return nextInt(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public int nextInt(int radix) {
        // Check cached result
        if ((typeCache != null) && (typeCache instanceof Integer)
            && this.radix == radix) {
            int val = ((Integer) typeCache).intValue();
            useTypeCache();
            return val;
        }
        setRadix(radix);
        clearCaches();
        // Search for next int
        try {
            String s = next(integerPattern());
            if (matcher.group(SIMPLE_GROUP_INDEX) == null) {
                s = processIntegerToken(s);
            }
            return Integer.parseInt(s, radix);
        } catch (NumberFormatException nfe) {
            position = matcher.start();
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * Returns true if the next token in this scanner's input can be
     * interpreted as a long value in the default radix using the
     * {@link #nextLong} method. The scanner does not advance past any input.
     *
     * @return true if and only if this scanner's next token is a valid
     * long value
     * @throws IllegalStateException if this scanner is closed
     */
    public boolean hasNextLong() {
        return hasNextLong(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public boolean hasNextLong(int radix) {
        setRadix(radix);
        boolean result = hasNext(integerPattern());
        if (result) {
            try {
                String s = (matcher.group(SIMPLE_GROUP_INDEX) == null) ?
                    processIntegerToken(hasNextResult) :
                    hasNextResult;
                typeCache = Long.parseLong(s, radix);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Scans the next token of the input as a <tt>long</tt>.
     *
     * <p> An invocation of this method of the form
     * <tt>nextLong()</tt> behaves in exactly the same way as the
     * invocation <tt>nextLong(radix)</tt>, where <code>radix</code>
     * is the default radix of this scanner.
     *
     * @return the <tt>long</tt> scanned from the input
     * @throws InputMismatchException if the next token does not match the <i>Integer</i>
     *                                regular expression, or is out of range
     * @throws NoSuchElementException if input is exhausted
     * @throws IllegalStateException  if this scanner is closed
     */
    public long nextLong() {
        return nextLong(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public long nextLong(int radix) {
        // Check cached result
        if ((typeCache != null) && (typeCache instanceof Long)
            && this.radix == radix) {
            long val = ((Long) typeCache).longValue();
            useTypeCache();
            return val;
        }
        setRadix(radix);
        clearCaches();
        try {
            String s = next(integerPattern());
            if (matcher.group(SIMPLE_GROUP_INDEX) == null) {
                s = processIntegerToken(s);
            }
            return Long.parseLong(s, radix);
        } catch (NumberFormatException nfe) {
            position = matcher.start();
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * The float token must be stripped of prefixes, group separators,
     * and suffixes, non ascii digits must be converted into ascii digits
     * before parseFloat will accept it.
     * <p>
     * If there are non-ascii digits in the token these digits must
     * be processed before the token is passed to parseFloat.
     */
    private String processFloatToken(String token) {
        String result = token.replaceAll(groupSeparator, "");
        if (!decimalSeparator.equals("\\.")) {
            result = result.replaceAll(decimalSeparator, ".");
        }
        boolean isNegative = false;
        int preLen = negativePrefix.length();
        if ((preLen > 0) && result.startsWith(negativePrefix)) {
            isNegative = true;
            result = result.substring(preLen);
        }
        int sufLen = negativeSuffix.length();
        if ((sufLen > 0) && result.endsWith(negativeSuffix)) {
            isNegative = true;
            result = result.substring(result.length() - sufLen,
                result.length());
        }
        if (result.equals(nanString)) {
            result = "NaN";
        }
        if (result.equals(infinityString)) {
            result = "Infinity";
        }
        if (isNegative) {
            result = "-" + result;
        }
        Matcher m = NON_ASCII_DIGIT.matcher(result);
        if (m.find()) {
            StringBuilder inASCII = new StringBuilder();
            for (int i = 0; i < result.length(); i++) {
                char nextChar = result.charAt(i);
                if (Character.isDigit(nextChar)) {
                    int d = Character.digit(nextChar, 10);
                    if (d != -1) {
                        inASCII.append(d);
                    } else {
                        inASCII.append(nextChar);
                    }
                } else {
                    inASCII.append(nextChar);
                }
            }
            result = inASCII.toString();
        }

        return result;
    }

    /**
     * @return
     */
    public boolean hasNextFloat() {
        setRadix(10);
        boolean result = hasNext(floatPattern());
        if (result) {
            try {
                String s = processFloatToken(hasNextResult);
                typeCache = Float.valueOf(Float.parseFloat(s));
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public float nextFloat() {
        if ((typeCache != null) && (typeCache instanceof Float)) {
            float val = ((Float) typeCache).floatValue();
            useTypeCache();
            return val;
        }
        setRadix(10);
        clearCaches();
        try {
            return Float.parseFloat(processFloatToken(next(floatPattern())));
        } catch (NumberFormatException nfe) {
            position = matcher.start();
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean hasNextDouble() {
        setRadix(10);
        boolean result = hasNext(floatPattern());
        if (result) {
            try {
                String s = processFloatToken(hasNextResult);
                typeCache = Double.valueOf(Double.parseDouble(s));
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public double nextDouble() {
        if ((typeCache != null) && (typeCache instanceof Double)) {
            double val = ((Double) typeCache).doubleValue();
            useTypeCache();
            return val;
        }
        setRadix(10);
        clearCaches();
        try {
            return Double.parseDouble(processFloatToken(next(floatPattern())));
        } catch (NumberFormatException nfe) {
            position = matcher.start();
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean hasNextBigInteger() {
        return hasNextBigInteger(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public boolean hasNextBigInteger(int radix) {
        setRadix(radix);
        boolean result = hasNext(integerPattern());
        if (result) {
            try {
                String s = (matcher.group(SIMPLE_GROUP_INDEX) == null) ?
                    processIntegerToken(hasNextResult) :
                    hasNextResult;
                typeCache = new BigInteger(s, radix);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public BigInteger nextBigInteger() {
        return nextBigInteger(defaultRadix);
    }

    /**
     * @param radix
     * @return
     */
    public BigInteger nextBigInteger(int radix) {
        if ((typeCache != null) && (typeCache instanceof BigInteger)
            && this.radix == radix) {
            BigInteger val = (BigInteger) typeCache;
            useTypeCache();
            return val;
        }
        setRadix(radix);
        clearCaches();
        try {
            String s = next(integerPattern());
            if (matcher.group(SIMPLE_GROUP_INDEX) == null) {
                s = processIntegerToken(s);
            }
            return new BigInteger(s, radix);
        } catch (NumberFormatException nfe) {
            position = matcher.start();
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public boolean hasNextBigDecimal() {
        setRadix(10);
        boolean result = hasNext(decimalPattern());
        if (result) { // Cache it
            try {
                String s = processFloatToken(hasNextResult);
                typeCache = new BigDecimal(s);
            } catch (NumberFormatException nfe) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return
     */
    public BigDecimal nextBigDecimal() {
        // Check cached result
        if ((typeCache != null) && (typeCache instanceof BigDecimal)) {
            BigDecimal val = (BigDecimal) typeCache;
            useTypeCache();
            return val;
        }
        setRadix(10);
        clearCaches();
        // Search for next float
        try {
            String s = processFloatToken(next(decimalPattern()));
            return new BigDecimal(s);
        } catch (NumberFormatException nfe) {
            position = matcher.start(); // don't skip bad token
            throw new InputMismatchException(nfe.getMessage());
        }
    }

    /**
     * @return
     */
    public CustomScanner reset() {
        delimPattern = WHITESPACE_PATTERN;
        useLocale(Locale.getDefault(Locale.Category.FORMAT));
        useRadix(10);
        clearCaches();
        return this;
    }
}
