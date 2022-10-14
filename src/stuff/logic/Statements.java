package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.LExecutorPlus.*;
import stuff.logic.Operations.*;

import static stuff.AdditionalFunction.*;
import static java.lang.Math.*;

public class Statements {
    public static class ComplexOperationStatement extends ShortStatement{
        public Func Op = Func.addC;
        public String RealOutput = "Re", ImaginaryOutput = "Im", r1 = "r1", i1 = "i1", r2 = "r2", i2 = "i2";
        
        public ComplexOperationStatement(String Op, String r1, String i1, String r2, String i2, String RealOutput, String ImaginaryOutput){
            try{
                this.Op = Func.valueOf(Op);
            }catch(Throwable ignored){}
            this.RealOutput = RealOutput;
            this.ImaginaryOutput = ImaginaryOutput;
            this.r1 = r1;
            this.i1 = i1;
            this.r2 = r2;
            this.i2 = i2;
        }

        public ComplexOperationStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            field2(table, RealOutput, str -> RealOutput = str);
            if(Op.isConstant){
                table.add(" = ");
                Button(table, table);
            }else{ 
                if (Op.SingleOutputCheck){}else{field2(table, ImaginaryOutput, str -> ImaginaryOutput = str);}
                table.add(" = ");
                if (Op.SingleInputCheck){Button(table, table);}else{row(table);}
                field2(table, r1, str -> r1 = str);
                field2(table, i1, str -> i1 = str);
                if (Op.SingleInputCheck != true){
                    Button(table, table);
                    field2(table, r2, str -> r2 = str);
                    field2(table, i2, str -> i2 = str);
                }
            }
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Op.symbol);
                b.clicked(() -> showSelect(b, Func.all, Op, o -> {
                    Op = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        public LInstruction build(LAssembler b){
            return new Function(Op, b.var(r1), b.var(i1), b.var(r2), b.var(i2), b.var(RealOutput), b.var(ImaginaryOutput));
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        public void write(StringBuilder builder){
            builder
                .append("Complex ")
                .append(Op.name())
                .append(" ")
                .append(r1)
                .append(" ")
                .append(i1)
                .append(" ")
                .append(r2)
                .append(" ")
                .append(i2)
                .append(" ")
                .append(RealOutput)
                .append(" ")
                .append(ImaginaryOutput)
                ;
            
        }

        @Override
        public LInstructionPlus buildplus(LAssembler builder) {
            return null;
        }
    }
    
    public static class VectorOperationsStatement extends ShortStatement{
        VFunction V = new VFunction();
        public VFunc Opv = VFunc.addV;
        public String[] 
        result = AlphabetFunction(V.n),
        a = Spam(V.n, "0"),
        b = Spam(V.n, "0");
        public String scalar = "scalar", n = "3";
       
        public VectorOperationsStatement(String Opv, String[] a, String[] b, String[] result, String scalar, String n){
            try{
                this.Opv = VFunc.valueOf(Opv);
            }catch(Throwable ignored){}
            this.a = a;
            this.b = b;
            this.result = result;
            this.scalar = scalar;
            this.n = n;
        }

        public VectorOperationsStatement(){}

        @Override
        public void build(Table table) {
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            if(Opv.scalar){
                for(int I2=0; I2<Integer.parseInt(n); I2++){
                    final int inI2 = I2;
                    if(I2 == ceil(V.n/2d)){
                        field2(table, scalar, str -> scalar = str);
                        table.add(" = ");
                    }else{table.add("   ");}
                    field2(table, a[I2], str -> a[inI2] = str);
                    if(I2 == ceil(V.n/2d)){Button(table, table);}else{table.add(" ");}
                    field2(table, b[I2], str -> b[inI2] = str);
                    row(table);
                }
            }else{
                for(int I=0; I<Integer.parseInt(n); I++){
                    final int inI = I;
                    field2(table, result[I], str -> result[inI] = str);
                    if(I == ceil(V.n/2d)){table.add(" = ");}else{table.add("   ");}
                    field2(table, a[I], str -> a[inI] = str);
                    if(I == ceil(V.n/2d)){Button(table, table);}else{table.add("   ");}
                    field2(table, b[I], str -> b[inI] = str);
                    if(I < 1){
                        table.add("N = ");
                        field2(table, n, str -> n = str);
                    }
                    row(table);
                }
                table.add("N = ");
                field2(table, n, str -> n = str);
            }
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Opv.symbol);
                b.clicked(() -> showSelect(b, VFunc.all, Opv, o -> {
                    Opv = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(120f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstructionPlus buildplus(LAssembler build) {
            return new VFunction(Opv, 
            GetVars(a),
            GetVars(b), 
            GetVars(result), 
            build.var(scalar),
            build.var(n)
            );
        }

        public void write(StringBuilder builder){
            builder
            .append("VectorOperation")
            .append(Opv.name());
            if(Opv.scalar){
                builder.append(scalar);
            }else{
                for(int u=0; u<Integer.parseInt(n); u++){
                    builder.append(result[u]);
                }
            }
            for(int u=0; u<Integer.parseInt(n); u++){
                builder.append(a[u]).append(b[u]);
            }
            builder.append(n);
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return null;
        }
        
    }

    public static void load(){
        registerStatement("Complex", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("Vector", args -> new VectorOperationsStatement(args[1], args, args, args, args[5], args[6]), VectorOperationsStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
