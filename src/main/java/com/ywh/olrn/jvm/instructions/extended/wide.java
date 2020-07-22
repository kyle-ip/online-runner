package com.ywh.olrn.jvm.instructions.extended;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.instructions.base.BytecodeReader;
import com.ywh.olrn.jvm.instructions.base.Instruction;
import com.ywh.olrn.jvm.instructions.base.InstructionFactory;
import com.ywh.olrn.jvm.instructions.loads.*;
import com.ywh.olrn.jvm.instructions.math.iinc;
import com.ywh.olrn.jvm.instructions.stores.*;
import com.ywh.olrn.jvm.rtda.Frame;

public class wide extends BranchInstruction {

    private Instruction modifiedInstruction;

    @Override
    public int getOpCode() {
        return 0xc4;
    }

    @Override
    public void fetchOperands(BytecodeReader reader) throws Exception {
        int opcode = reader.read8();
        switch (opcode) {
            case 0x15:
                iload a = (iload) InstructionFactory.getByOpcode(0x15);
                a.setIndex(reader.read16());
                modifiedInstruction = a;
                break;
            case 0x16:
                lload b = (lload) InstructionFactory.getByOpcode(0x16);
                b.setIndex(reader.read16());
                modifiedInstruction = b;
                break;
            case 0x17:
                fload c = (fload) InstructionFactory.getByOpcode(0x17);
                c.setIndex(reader.read16());
                modifiedInstruction = c;
                break;
            case 0x18:
                dload d = (dload) InstructionFactory.getByOpcode(0x18);
                d.setIndex(reader.read16());
                modifiedInstruction = d;
                break;
            case 0x19:
                aload e = (aload) InstructionFactory.getByOpcode(0x19);
                e.setIndex(reader.read16());
                modifiedInstruction = e;
                break;
            case 0x36:
                istore f = (istore) InstructionFactory.getByOpcode(0x36);
                f.setIndex(reader.read16());
                modifiedInstruction = f;
                break;
            case 0x37:
                lstore g = (lstore) InstructionFactory.getByOpcode(0x37);
                g.setIndex(reader.read16());
                modifiedInstruction = g;
                break;
            case 0x38:
                fstore h = (fstore) InstructionFactory.getByOpcode(0x38);
                h.setIndex(reader.read16());
                modifiedInstruction = h;
                break;
            case 0x39:
                dstore i = (dstore) InstructionFactory.getByOpcode(0x39);
                i.setIndex(reader.read16());
                modifiedInstruction = i;
                break;
            case 0x3a:
                astore j = (astore) InstructionFactory.getByOpcode(0x3a);
                j.setIndex(reader.read16());
                modifiedInstruction = j;
                break;
            case 0x84:
                iinc k = (iinc) InstructionFactory.getByOpcode(0x64);
                k.setIndex(reader.read16());
                k.setConstVal(reader.read16());
                modifiedInstruction = k;
                break;
            case 0xa9:
                throw new Exception("error");
        }
    }

    @Override
    public void execute(Frame frame) throws Exception {
        modifiedInstruction.execute(frame);
    }
}
