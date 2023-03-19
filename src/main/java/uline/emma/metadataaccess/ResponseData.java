package uline.emma.metadataaccess;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ResponseData {
    @JsonProperty("Row")
    private List<Map<String, Object>> Row;

    public List<Map<String, Object>> getRow() {
        return Row;
    }

    public void setRow(List<Map<String, Object>> row) {
        Row = row;
    }
}