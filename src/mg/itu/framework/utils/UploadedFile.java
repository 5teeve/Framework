package mg.itu.framework.utils;

public class UploadedFile {

    private String format;
    private byte[] data;

    public UploadedFile(String format, byte[] data) {
        this.format = format;
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
