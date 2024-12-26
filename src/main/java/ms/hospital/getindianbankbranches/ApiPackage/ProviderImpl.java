package ms.hospital.getindianbankbranches.ApiPackage;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderImpl {
    protected JSONObject dataProvider() throws IOException {
        String EXCEL_FILE_PATH = "src\\main\\resources\\static\\IBAllBranchDetails.xls";
        InputStream inputStream = new FileInputStream(EXCEL_FILE_PATH);
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
}
