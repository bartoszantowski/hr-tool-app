package com.iitrab.hrtool.project.api;

import com.iitrab.hrtool.client.api.ClientAssert;
import org.assertj.core.api.AbstractAssert;
import org.eclipse.jdt.annotation.Nullable;

import java.util.function.Consumer;

import static com.iitrab.hrtool.client.api.ClientAssert.assertThatClient;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class ProjectAssert extends AbstractAssert<ProjectAssert, Project> {

    private ProjectAssert(Project project) {
        super(project, ProjectAssert.class);
    }

    public static ProjectAssert assertThatProject(Project project) {
        return new ProjectAssert(project);
    }

    public ProjectAssert hasId(@Nullable Long id) {
        isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        return this;
    }

    public ProjectAssert hasName(String name) {
        isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public void isForClientThat(Consumer<ClientAssert> client) {
        isNotNull();
        client.accept(assertThatClient(actual.getClient()));
    }

}