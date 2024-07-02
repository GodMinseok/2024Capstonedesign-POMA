package goinmul.sportsmanage.service;

import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.repository.GroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroundService {

    private final GroundRepository groundRepository;

    @Transactional
    public boolean saveGround(Ground ground) {
        Ground findGround = groundRepository.validateGround(ground.getLocation(), ground.getName());
        if(findGround == null){
            groundRepository.save(ground);
            return true;
        } else return false;
    }

    @Transactional
    public boolean updateGround(Long targetGroundId, String location, String name, Integer price) {
        Ground findGround = groundRepository.validateGround(location, name);
        //운동장 가격만 바꾸는 상황 고려
        if(findGround == null || findGround.getId().equals(targetGroundId)) {
            groundRepository.update(targetGroundId, location, name, price);
            return true;
        }else return false;
    }

}
