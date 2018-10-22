package com.poseidon.fridge.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.poseidon.fridge.member.model.Member;
import com.poseidon.fridge.member.repository.MemberRepository;

@Configuration
public class MemberRepositoryRestConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config
            .exposeIdsFor(Member.class)
            .withEntityLookup()
            .forRepository(MemberRepository.class, Member::getUsername, MemberRepository::findByUsername);
    }

}
