package com.springdemo.hogwartsartifactsonline.system;

import com.springdemo.hogwartsartifactsonline.artifact.Artifact;
import com.springdemo.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.springdemo.hogwartsartifactsonline.user.User;
import com.springdemo.hogwartsartifactsonline.user.UserRepository;
import com.springdemo.hogwartsartifactsonline.wizard.Wizard;
import com.springdemo.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    private final UserRepository userRepository;
    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserRepository userRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Delauniator");
        a1.setDescription("A Deluminator is a device invented by Albus");
        a1.setImageUrl("ImageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Time Turner");
        a3.setDescription("A Time Turner is a magical time-travel device.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("Pensieve");
        a4.setDescription("A Pensieve is a magical object to review and enter memories.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("Marauder's Map");
        a5.setDescription("The Marauder's Map is a magical document revealing Hogwarts School of Witchcraft and Wizardry.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Portkey");
        a6.setDescription("A Portkey is an enchanted object for magical transportation.");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Nevile Longbottom");
        w3.addArtifact(a5);

        User u1 = new User();
        u1.setId(1);
        u1.setUsername("john");
        u1.setRoles("admin user");
        u1.setPassword("ABCDEfghi123456*()");
        u1.setEnabled(true);

        User u2 = new User();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setRoles("user");
        u2.setPassword("ABCDEfghi123456*()");
        u2.setEnabled(true);

        User u3 = new User();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setRoles("user");
        u3.setPassword("ABCDEfghi123456*()");
        u3.setEnabled(false);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a6);

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);

    }
}
