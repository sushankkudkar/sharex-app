package com.sharex.app.module.user.application.command.handler;

import com.sharex.app.module.user.application.command.CreateUserCommand;
import com.sharex.app.module.user.application.dto.UserView;
import com.sharex.app.module.user.domain.User;
import com.sharex.app.module.user.port.in.UserCommandPort;
import com.sharex.app.module.user.port.out.UserWriteRepositoryPort;
import com.sharex.app.shared.event.EventPublisher;
import com.sharex.app.shared.web.error.ErrorCode;
import com.sharex.app.shared.web.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserCommandHandler implements UserCommandPort {

    private final UserWriteRepositoryPort writeRepo;
    private final EventPublisher eventPublisher;

    public CreateUserCommandHandler(UserWriteRepositoryPort writeRepo,
                                    EventPublisher eventPublisher) {
        this.writeRepo = writeRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserView createUser(CreateUserCommand command) {

        if (writeRepo.existsByEmail(command.email())) {
            throw new BusinessException(
                    "User with this email already exists",
                    ErrorCode.BUSINESS_RULE_VIOLATION
            );
        }

        String id = UUID.randomUUID().toString();


        User user = new User(
                id,
                command.name(),
                command.email()
        );

        writeRepo.save(user);

        eventPublisher.publishBatch(user.getEvents());

        return UserView.fromDomain(user);
    }
}
