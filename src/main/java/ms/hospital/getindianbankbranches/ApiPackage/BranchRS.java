package ms.hospital.getindianbankbranches.ApiPackage;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/IB")
public class BranchRS {

    @RequestMapping(value = "/fetchDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String fetchDetails() throws IOException {
        return new ProviderImpl().dataProvider().toString();
    }
}
