package io.github.thewebcode.honey.gui.widget;

public class HToggleButton implements HWidget {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String text;
    private final boolean toggled;
    private final String buttonID;

    public HToggleButton(String text, String buttonID, boolean toggled, int x, int y, int width, int height) {
        this.text = text;
        this.buttonID = buttonID;
        this.toggled = toggled;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public HToggleButton(String text, String buttonID, int x, int y, int width, int height) {
        this(text, buttonID, false, x, y, width, height);
    }

    public HToggleButton(String text, String buttonID, boolean toggled, int x, int y) {
        this(text, buttonID, toggled, x, y, 1, 1);
    }

    public HToggleButton(String text, String buttonID, int x, int y) {
        this(text, buttonID, false, x, y, 1, 1);
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getText() {
        return text;
    }

    public String getButtonID() {
        return buttonID;
    }

    @Override
    public int posX() {
        return x;
    }

    @Override
    public int posY() {
        return y;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int heigth() {
        return height;
    }

    @Override
    public Type widgetType() {
        return Type.TOGGLE_BUTTON;
    }
}
