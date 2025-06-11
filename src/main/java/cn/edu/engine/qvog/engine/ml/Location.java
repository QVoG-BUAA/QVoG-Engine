package cn.edu.engine.qvog.engine.ml;

public class Location {
    private final String file;
    private final int lineno;
    private final PredictTypes type;

    public Location(String file, int lineno, PredictTypes type) {
        this.file = file;
        this.lineno = lineno;
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public int getLineno() {
        return lineno;
    }

    public PredictTypes getType() {
        return type;
    }
}
