package models;

import java.util.UUID;

/**
 * As additional features are added to service, this likely inherits from currently undefined interface.
 * I'd also move this to an intermediate type, not the final response being marshalled
 * @author cgrass
 *
 */
public class BuyResponse {

	public boolean success;
	public UUID txnId;
	public String message;
}
