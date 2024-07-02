package goinmul.sportsmanage.domain.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Getter @Setter
public class JoinMercenaryMatchForm {

    private List<Long> userIdList;
    private Integer mercenaryMaxSize;
}
