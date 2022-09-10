
package petfriends.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import petfriends.payment.model.Payment;

@FeignClient(name="External", url="${api.url.external}")
public interface ExternalService {

    @RequestMapping(method= RequestMethod.POST, path="/external")
    public String card_pay(@RequestBody Payment payment);
    
    
    @RequestMapping(method= RequestMethod.PUT, path="/external/cancel/{cardPpprovalNumber}")
    public boolean card_cancel(@PathVariable String cardPpprovalNumber);
    

}