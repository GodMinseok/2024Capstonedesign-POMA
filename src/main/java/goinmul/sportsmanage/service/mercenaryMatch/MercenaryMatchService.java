package goinmul.sportsmanage.service.mercenaryMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.mercenaryMatch.*;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSizeRepository;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryMatchService {

    private final MercenaryMatchRepository mercenaryMatchRepository;
    private final MercenaryMatchUserRepository mercenaryUserRepository;
    private final MercenaryMatchTeamMercenaryMaxSizeRepository mercenaryMatchTeamMercenaryMaxSizeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void saveMercenaryMatch(MercenaryMatch mercenaryMatch) {
        mercenaryMatchRepository.save(mercenaryMatch);
    }

    // [GET] mercenary/{sports}에서 사용
    public List<MercenaryMatch> findMercenaryMatchWithAllEntityAndTeamListByDateAndLocationAndGender(LocalDate date, String location, Gender gender, Sports sports) {
        //mercernaryMatchUser랑 조인 돼있음(총 신청인원 필요해서)
        List<MercenaryMatch> mercenaryMatches = mercenaryMatchRepository.findWithAllEntityByDateAndLocationAndGender(date, location, gender, sports);

        //WHERE IN 쿼리에 쓸 매치 ID 리스트 만들기
        List<Long> mercenaryMatchIdList = new ArrayList<>();
        for (MercenaryMatch mercenaryMatch : mercenaryMatches) {
            mercenaryMatchIdList.add(mercenaryMatch.getId());
        }

        //TeamsWithMercenary 리스트 만들기
        //Step1 팀이랑 조인함 (팀 리스트 만들때 조인 필요)
        List<MercenaryMatchTeamMercenaryMaxSize> mercenaryMatchTeamMercenaryMaxSizes = mercenaryMatchTeamMercenaryMaxSizeRepository.findWithTeamByMercenaryMatchIdIn(mercenaryMatchIdList);
        //Step2 Map Long은 매치ID
        Map<Long, List<TeamWithMercenary>> map = new HashMap<>();
        List<Team> teams = new ArrayList<>();
        for (MercenaryMatch mercenaryMatch : mercenaryMatches) {
            List<TeamWithMercenary> list = new ArrayList<>();
            for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
                if (!teams.contains(mercenaryMatchUser.getTeam())) {
                    teams.add(mercenaryMatchUser.getTeam());
                    TeamWithMercenary teamWithMercenary = null;
                    if (mercenaryMatchUser.getStatus().equals(UserStatus.TEAM)) {
                        teamWithMercenary = TeamWithMercenary.createTeamWithMercenary(mercenaryMatchUser.getTeam());
                    } else {
                        teamWithMercenary = TeamWithMercenary.createTeamWithMercenary(mercenaryMatchUser.getTeam(), mercenaryMatchUser);
                    }
                    if (map.get(mercenaryMatchUser.getMercenaryMatch().getId()) == null) {
                        list.add(teamWithMercenary);
                        map.put(mercenaryMatchUser.getMercenaryMatch().getId(), list);
                    } else
                        map.get(mercenaryMatchUser.getMercenaryMatch().getId()).add(teamWithMercenary);
                } else {
                    if (mercenaryMatchUser.getStatus().equals(UserStatus.MERCENARY)) {
                        for (TeamWithMercenary teamWithMercenaryValue : map.get(mercenaryMatchUser.getMercenaryMatch().getId())) {
                            if (mercenaryMatchUser.getTeam().equals(teamWithMercenaryValue.getTeam()))
                                teamWithMercenaryValue.getMercenaries().add(mercenaryMatchUser);
                        }
                    }
                }
            }
            teams.clear();
        }
        //Step3 용병 모집 인원 할당
        for (MercenaryMatchTeamMercenaryMaxSize mmtmms : mercenaryMatchTeamMercenaryMaxSizes) {
            for (TeamWithMercenary teamWithMercenaryValue : map.get(mmtmms.getMercenaryMatch().getId())) {
                if (mmtmms.getTeam().equals(teamWithMercenaryValue.getTeam()))
                    teamWithMercenaryValue.changeMaxSize(mmtmms.getMaxSize());
            }
        }
        //Step4(완성) 다 만든거 MercenaryMatch에 할당
        for (MercenaryMatch mercenaryMatch : mercenaryMatches) {
            mercenaryMatch.getTeamsWithMercenary().addAll(map.get(mercenaryMatch.getId()));
        }

        return mercenaryMatches;
    }

    //용병 신청 POST & 용병 매치 멤버 선택 GET
    public MercenaryMatch findWithMercenaryMaxSizeAndMercenaryUser(Long mercenaryMatchId) {
        MercenaryMatch mercenaryMatch = mercenaryMatchRepository.findWithMercenaryUserByMercenaryMatchId(mercenaryMatchId);
        List<MercenaryMatchTeamMercenaryMaxSize> mercenaryMaxSizeWithTeam = mercenaryMatchTeamMercenaryMaxSizeRepository.findWithTeamByMercenaryMatchId(mercenaryMatchId);

        //MercenaryMatch의 teamWithMercenary 할당
        for (MercenaryMatchTeamMercenaryMaxSize mmswm : mercenaryMaxSizeWithTeam) {
            TeamWithMercenary teamWithMercenary = TeamWithMercenary.createTeamWithMercenary(mmswm.getTeam(), mmswm.getMaxSize());
            mercenaryMatch.getTeamsWithMercenary().add(teamWithMercenary);
        }

        List<TeamWithMercenary> teamWithMercenaryList = mercenaryMatch.getTeamsWithMercenary();
        for (MercenaryMatchUser mercenaryMatchUser : mercenaryMatch.getMercenaryMatchUsers()) {
            if (mercenaryMatchUser.getStatus().equals(UserStatus.MERCENARY)) {
                if (mercenaryMatchUser.getTeam().getId().equals(teamWithMercenaryList.get(0).getTeam().getId())) {
                    teamWithMercenaryList.get(0).getMercenaries().add(mercenaryMatchUser);
                } else teamWithMercenaryList.get(1).getMercenaries().add(mercenaryMatchUser);
            }
        }

        return mercenaryMatch;
    }

}
