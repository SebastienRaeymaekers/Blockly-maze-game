package blockr;

import GameWorldApi.ExecuteResult;
import blockr.block.*;
import blockr.connector.BodyConnector;
import blockr.connector.Connector;
import blockr.connector.DownConnector;
import blockr.connector.LeftConnector;

import java.awt.*;
import java.util.ArrayList;

// Responsible for drawing all objects stored in the backend.
public class GUIDrawer {

    private static final Color paletteColor = new Color(212, 212, 212);
    private static final Color programAreaColor = new Color(225, 225, 225);

    private static final Color highlightedColor = new Color(255, 220, 107);
    private static final Color moveBlockColor = new Color(28, 40, 173);
    private static final Color turnLeftBlockColor = new Color(141, 61, 150);
    private static final Color turnRightBlockColor = new Color(141, 61, 150);
    private static final Color ifBlockColor = new Color(214, 174, 67);
    private static final Color whileBlockColor = new Color(28, 163, 45);
    private static final Color notBlockColor = new Color(219, 217, 81);
    private static final Color wallInFrontBlockColor = new Color(153, 37, 40);
    private static final Color functionDefinitionBlockColor = new Color(20, 137, 240);
    private static final Color functionCallBlockColor = new Color(153, 153, 255);
    private static final Color bodyConnectorColor = Color.CYAN;
    private static final Color gameCompletedColor = new Color(42, 178, 42);

    private static final float defaultFontSize = 12;
    private static final float mediumFontSize = 25;
    private static final float largeFontSize = 40;

    private static final Color blockTextColor = Color.WHITE;
    private static final String moveBlockText = "Move";
    private static final String turnLeftBlockText = "Turn left";
    private static final String turnRightBlockText = "Turn right";
    private static final String ifBlockText = "If";
    private static final String whileBlockText = "While";
    private static final String notBlockText = "Not";
    private static final String wallInFrontBlockText = "Wall in front";
    private static final String functionDefinitionBlockText = "Function Def: ";
    private static final String functionCallBlockText = "Function Call: ";
    private static final String blocksRemainingText = " blocks remaining";
    private static final String gameCompletedText = "Game completed!";

    public static void paintGame(Graphics g, AreaController areaController, ExecutionController executionController) {
        paint(g, areaController);
        if (executionController.getGameStatus() == ExecuteResult.COMPLETED) paintGameOverMessage(g, areaController);
    }

    private static void paint(Graphics g, AreaController areaController) {
        setFontSize(g, defaultFontSize);
        paint(g, areaController.getProgramArea());
        paint(g, areaController.getPalette());
        areaController.getGameWorld().paint(g);
        paintBlocksRemaining(g, areaController);
        if (areaController.getDraggedBlock() != null) paint(g, areaController.getDraggedBlock().getBlocksInScope());
    }

    private static void paintGameOverMessage(Graphics g, AreaController ac) {
        setFontSize(g, largeFontSize);
        int[] bounds = ac.getGameWorldBounds();
        int textWidth = g.getFontMetrics().stringWidth(gameCompletedText);
        g.setColor(gameCompletedColor);
        g.drawString(gameCompletedText, (bounds[0] + bounds[2] - textWidth) / 2, bounds[1] + bounds[3] / 2);
        setFontSize(g, defaultFontSize);
    }

    private static void paintBlocksRemaining(Graphics g, AreaController ac) {
        setFontSize(g, mediumFontSize);
        g.setColor(Color.BLACK);
        int[] palBounds = ac.getPalette().getBounds();
        g.drawString(ac.getAvailableBlocks() + blocksRemainingText, 10 + palBounds[0], palBounds[1] + palBounds[3] - 10);
        setFontSize(g, defaultFontSize);
    }

    private static void paint(Graphics g, Palette pal) {
        g.setColor(paletteColor);
        g.fillRect(pal.getBounds()[0], pal.getBounds()[1], pal.getBounds()[2], pal.getBounds()[3]);
        paint(g, pal.getBlocks());
    }

    private static void paint(Graphics g, ProgramArea prgArea) {
        g.setColor(programAreaColor);
        g.fillRect(prgArea.getBounds()[0], prgArea.getBounds()[1], prgArea.getBounds()[2], prgArea.getBounds()[3]);
        paint(g, prgArea.getBlocks());
    }

    private static void paint(Graphics g, ArrayList<Block> blocks) {
        for (Block blockToDraw : blocks) {
            paint(g, blockToDraw);
        }
    }

    private static void paint(Graphics g, Block block) {
        g.setColor(getBlockColor(block));
        g.translate(block.getPosition()[0], block.getPosition()[1]);
        g.fillRect(0, 0, block.getPosition()[2], block.getPosition()[3]);

        String blockText = getBlockText(block);
        int textWidth = g.getFontMetrics().stringWidth(blockText);
        g.setColor(blockTextColor);
        g.drawString(blockText, (block.getPosition()[2] - textWidth) / 2, 30);
        g.translate(-block.getPosition()[0], -block.getPosition()[1]);

        for (Connector connectorToDraw : block.getConnectors()) paint(g, connectorToDraw);
    }

    private static void paint(Graphics g, Connector connector) {
        g.setColor(getConnectorColor(connector));
        g.fillRect(connector.getPosition()[0], connector.getPosition()[1], connector.getPosition()[2], connector.getPosition()[3]);
    }

    private static Color getBlockColor(Block block) {
        // if only we could switch on types of a class
        if (block.isHighlighted()) return highlightedColor;
        else if (block instanceof MoveBlock) return moveBlockColor;
        else if (block instanceof TurnLeftBlock) return turnLeftBlockColor;
        else if (block instanceof TurnRightBlock) return turnRightBlockColor;
        else if (block instanceof IfBlock) return ifBlockColor;
        else if (block instanceof WhileBlock) return whileBlockColor;
        else if (block instanceof NotBlock) return notBlockColor;
        else if (block instanceof WallInFrontBlock) return wallInFrontBlockColor;
        else if (block instanceof FunctionDefinitionBlock) return functionDefinitionBlockColor;
        else if (block instanceof FunctionCallBlock) {
            int newGreenChannel = functionCallBlockColor.getGreen() - ((FunctionCallBlock) block).getUniqueID() * 7;
            if (newGreenChannel < 0) newGreenChannel = 0;
            return new Color(functionCallBlockColor.getRed(), newGreenChannel, functionCallBlockColor.getBlue());
        } else return null;
    }

    private static String getBlockText(Block block) {
        if (block instanceof MoveBlock) return moveBlockText;
        else if (block instanceof TurnLeftBlock) return turnLeftBlockText;
        else if (block instanceof TurnRightBlock) return turnRightBlockText;
        else if (block instanceof IfBlock) return ifBlockText;
        else if (block instanceof WhileBlock) return whileBlockText;
        else if (block instanceof NotBlock) return notBlockText;
        else if (block instanceof WallInFrontBlock) return wallInFrontBlockText;
        else if (block instanceof FunctionDefinitionBlock)
            return functionDefinitionBlockText + ((FunctionDefinitionBlock) block).getUniqueID();
        else if (block instanceof FunctionCallBlock)
            return functionCallBlockText + ((FunctionCallBlock) block).getUniqueID();
        else return "UNKNOWN";
    }

    private static Color getConnectorColor(Connector connector) {
        if (connector.isHighlighted()) {
            connector.setHighlight(false);
            return highlightedColor;
        } else if (connector instanceof BodyConnector) return bodyConnectorColor;
        else if (connector instanceof DownConnector || connector instanceof LeftConnector)
            return getBlockColor(connector.getBlock());
        else if (connector.getConnectedTo() != null) return getBlockColor(connector.getConnectedTo().getBlock());
        else return programAreaColor;
    }

    private static void setFontSize(Graphics g, float fontSize) {
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(fontSize);
        g.setFont(newFont);
    }
}
