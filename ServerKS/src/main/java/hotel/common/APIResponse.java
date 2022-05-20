package hotel.common;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class APIResponse {
	private Integer status;
	private Object data;
	private Object error;

	public APIResponse() {
		super();
		this.status = HttpStatus.OK.value();
	}

}
