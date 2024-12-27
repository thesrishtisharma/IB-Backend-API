package ms.hospital.getindianbankbranches.ApiPackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/IB")
public class OpenApiRS {

    @Autowired
    private ProviderImpl provider;

    @RequestMapping(value = "/fetchZones", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getIBZones(){
        JSONObject zones = this.provider.getUniqueZones();
        if(zones != null){
            return zones.toString();
        }
        ObjectNode errorNode = new ObjectMapper().createObjectNode();
        errorNode.put("code", "Failed");
        errorNode.put("message", "No IB Zones Found");
        return errorNode.toString();
    }

    @RequestMapping(value = "/fetchBranches", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String fetchBranches(@RequestParam("zone") String zone) {
        JSONObject data = this.provider.fetchBranchData(zone);
        if(data != null) {
            return data.toString();
        }
        ObjectNode errorNode = new ObjectMapper().createObjectNode();
        errorNode.put("status", "Failed");
        errorNode.put("message", "An error occured");
        return errorNode.toString();
    }
}
