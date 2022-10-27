package main.service;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import main.dto.GroupDto;
import main.entity.GroupEntity;
import main.entity.TeamEntity;
import main.request.CreateGroupRequest;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GroupService {

    private final ModelMapper modelMapper = new ModelMapper();

    public GroupService() {
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public  List<GroupDto> getAllGroups() {
        List<GroupEntity>  resultList =  GroupEntity.findAll().list();
        List<TeamEntity> teamEntities = TeamEntity.findAll().list();
        resultList.forEach(groupEntity -> {
            List<TeamEntity> optionalTeamList = teamEntities.stream().filter(teamEntity -> teamEntity.getGroupEntity().getGroupName().equals(groupEntity.getGroupName())).collect(Collectors.toList());
            groupEntity.setTeams(new HashSet<>(optionalTeamList));
        });

        // Use Modelmapper to map all the entities to DTOs
        List<GroupDto> result = resultList.stream().map(groupEntity -> modelMapper.map(groupEntity, GroupDto.class)).collect(Collectors.toList());
        return  result;
    }
    @Transactional
    public Optional<GroupEntity> createNewGroup(CreateGroupRequest createGroupRequest) {
        GroupEntity groupEntity = modelMapper.map(createGroupRequest, GroupEntity.class);
        groupEntity.setTeams(new HashSet<>());
        GroupEntity.persist(groupEntity);
        return Optional.ofNullable(groupEntity);
    }

    @Transactional
    public Optional<GroupEntity> updateGroup(GroupEntity groupEntity) {
        groupEntity.persistAndFlush();
        return Optional.ofNullable(groupEntity);
    }

    @Transactional
    public boolean deleteGroup(UUID uuid) {
        return GroupEntity.deleteById(uuid);
    }
}
