package main.service;

import main.dto.TeamDto;
import main.entity.TeamEntity;
import main.request.TeamCreateRequest;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TeamService {

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public TeamEntity saveNewTeam(TeamCreateRequest teamCreateRequest) {
        var teamEntity = modelMapper.map(teamCreateRequest, TeamEntity.class);
        teamEntity.persistAndFlush();
        return teamEntity;
    }


    public List<TeamDto> getTeamList() {
        List<TeamEntity> teamEntityList = TeamEntity.listAll();
        return teamEntityList.stream().map(teamEntity -> modelMapper.map(teamEntity, TeamDto.class)).toList();
    }
}
