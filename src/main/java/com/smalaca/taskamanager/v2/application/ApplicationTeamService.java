package com.smalaca.taskamanager.v2.application;

import com.smalaca.taskamanager.dto.TeamDto;
import com.smalaca.taskamanager.exception.TeamNotFoundException;
import com.smalaca.taskamanager.model.entities.Team;
import com.smalaca.taskamanager.model.entities.User;
import com.smalaca.taskamanager.repository.TeamRepository;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ApplicationTeamService {
    private final TeamRepository teamRepository;

    public ApplicationTeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Optional<TeamDto> findById(Long teamId) {
        try {
            Team team = getTeamById(teamId);
            TeamDto dto = new TeamDto();
            dto.setId(team.getId());
            dto.setName(team.getName());

            if (team.getCodename() != null) {
                dto.setCodenameShort(team.getCodename().getShortName());
                dto.setCodenameFull(team.getCodename().getFullName());
            }

            dto.setDescription(team.getDescription());
            dto.setUserIds(team.getMembers().stream().map(User::getId).collect(toList()));
            return Optional.of(dto);

        } catch (TeamNotFoundException exception) {
            return Optional.empty();
        }
    }

    private Team getTeamById(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isEmpty()) {
            throw new TeamNotFoundException();
        }

        return team.get();
    }
}
