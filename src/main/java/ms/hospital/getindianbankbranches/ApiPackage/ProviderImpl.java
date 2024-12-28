package ms.hospital.getindianbankbranches.ApiPackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProviderImpl {

    private final String EXCEL_FILE_PATH = "src\\main\\resources\\static\\IBAllBranchDetails.xls";
    private final String JSON_FILE_PATH = "src\\main\\resources\\static\\IBAllBranchDetails.json";
    protected JSONObject dataProvider(ObjectNode request) throws IOException {
        if(!this.validateUserId(request)) return null;
        return this.convertExcelDataToJson();
    }

    private boolean validateUserId(ObjectNode request){
        return request.get("userId").toString().equals("\"A1179\"");
    }

    private JSONObject convertExcelDataToJson() throws IOException{
        InputStream inputStream = this.readFile(EXCEL_FILE_PATH);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<String> headers = new ArrayList<>();
        for(Cell cell: sheet.getRow(1)){
            headers.add(cell.getStringCellValue());
        }

        JSONArray dataArray = new JSONArray();

        for(int i = 2; i < sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);
            JSONObject dataObj = new JSONObject();

            if(row.getCell(1).toString().isEmpty() || row.getCell(1) == null)
                break;
            for(int j = 0; j < headers.size(); j++){
                if(row.getCell(j).getCellType().equals(CellType.NUMERIC))
                    dataObj.put(headers.get(j), row.getCell(j).getNumericCellValue());
                else
                    dataObj.put(headers.get(j), row.getCell(j).getStringCellValue());
            }
            dataArray.put(dataObj);
        }
        return new JSONObject().put("data", dataArray);
    }

    protected JSONObject getUniqueZones(){
        JSONArray data = this.readJsonData();
        if(data == null) return null;

        List<String> zones = IntStream.range(0, data.length())
                .mapToObj(i -> data.getJSONObject(i))
                .map(obj -> obj.getString("Zone"))
                .collect(Collectors.toSet())    // created a set to carry only unique zones
                .stream().sorted().collect(Collectors.toList());


        return new JSONObject().put("zones", zones);
    }

    protected JSONObject fetchBranchData(String zone){
        JSONArray data = this.readJsonData();
        if(data == null) return null;

        JSONArray zoneData = new JSONArray();
        IntStream.range(0, data.length())
                .filter(i -> data.getJSONObject(i).get("Zone").toString().toLowerCase().contains(zone.toLowerCase()))
                .forEach(i -> zoneData.put(data.getJSONObject(i)));

        return new JSONObject().put("branchData", zoneData);
    }

    private JSONArray readJsonData(){
        try {
            InputStream inputStream = this.readFile(JSON_FILE_PATH);
            ObjectNode node = new ObjectMapper().readValue(inputStream, ObjectNode.class);
            JSONObject bankDetails = new JSONObject(node.toString());
            JSONArray data = bankDetails.getJSONArray("data");
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream readFile(String path) throws FileNotFoundException {
        return new FileInputStream(path);
    }
}