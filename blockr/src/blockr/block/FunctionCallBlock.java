package blockr.block;

public class FunctionCallBlock extends ActionBlock{

    private int uniqueID;

    public FunctionCallBlock(int[] dimensions, int id) {
        super(dimensions);
        setUniqueID(id);
    }

    public void setUniqueID(int id) {
        uniqueID = id;
    }

    public int getUniqueID(){
        return uniqueID;
    }

}
