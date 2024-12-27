package ms.hospital.getindianbankbranches.ApiPackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/IB/private")
public class BranchRS {

    @RequestMapping(value = "/populateData", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode fetchDetails(@RequestBody ObjectNode requestBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        JSONObject data = new ProviderImpl().dataProvider(requestBody);
        if(data == null) {
            root.put("status", "error");
            root.put("message", "An error occured");
            return root;
        }
        return this.writeJsonFile(mapper, data.toString());
    }

    private ObjectNode writeJsonFile(ObjectMapper mapper, String data) throws IOException {
        ObjectNode root = mapper.readValue(data, ObjectNode.class);
        mapper.writeValue(new File("src\\main\\resources\\static\\IBAllBranchDetails.json"), root);
        root.removeAll();
        root.put("status", "success");
        root.put("message", "JSON file created successfully");
        return root;
    }

}
