package framework.binding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UploadFile {
    private String filename;
    private String extension;
    private byte[] bytes;

    public UploadFile() {
    }

    public UploadFile(String filename, String extension, byte[] bytes) {
        this.filename = filename;
        this.extension = extension;
        this.bytes = bytes;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getContentAsString() {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "filename='" + filename + '\'' +
                ", extension='" + extension + '\'' +
                ", size=" + (bytes == null ? 0 : bytes.length) +
                '}';
    }
}