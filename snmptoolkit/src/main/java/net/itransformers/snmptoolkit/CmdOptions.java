
package net.itransformers.snmptoolkit;

public enum CmdOptions {
    ADDRESS("a", 1),
    COMMUNITY("c", 1),
    VERSION("v", 1),
    TIMEOUT("t", 1),
    RETRIES("r", 1),
    MAX_REPETITIONS("m", 1),
    OUTPUT_FILE("f", 1),
    PORT ("p",1),
    OIDS("o", 1),
    DELTA("d", 1),
    MIBS_DIR("md", 1),
    PRINT_LOADED_MIBS("pm", 0);

    private final String name;
    private final int valueSize;

    CmdOptions(String value, int valueSize) {
        this.name = value;
        this.valueSize = valueSize;
    }

    public String getName() {
        return name;
    }

    public int getValueSize() {
        return valueSize;
    }
}
