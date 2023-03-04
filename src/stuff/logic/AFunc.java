package stuff.logic;

public enum AFunc{
    // Array section
    New("new"),
    Add("+"),
    Subtract("-"),
    Muliply("*"),
    Divide("/"),
    DotProd("·"),
    CrossProduct("x"),
    SumAll("sum"),
    ProductAll("prod"),
    Change("change", true),
    Get("get"),
    Shuffle("shuffle", true),
    Resize("resize"),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final boolean local;

    @Override
    public String toString(){
        return symbol;
    }

    AFunc(String symbol){
        this.symbol = symbol;
        this.local = false;
    }

    AFunc(String symbol, boolean local){
        this.symbol = symbol;
        this.local = local;
    }

    public static enum TwoType{
        array,
        number;

        public static final TwoType[] all = values();
    }
}
