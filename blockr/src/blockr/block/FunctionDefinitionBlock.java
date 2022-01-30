package blockr.block;

import blockr.connector.BodyConnector;

public class FunctionDefinitionBlock extends NestedBlock {

    private int uniqueID;

    public FunctionDefinitionBlock(int[] dimensions, int id) {
        super(dimensions);
        setUniqueID(id);
        addConnector(new BodyConnector(this, new int[] {50, 50, 20, 10}));
    }

    public void setUniqueID(int id) {
        uniqueID = id;
    }

    public int getUniqueID(){
        return uniqueID;
    }

}

