package org.ricardoft.scrumpoker;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScrumPokerTest {
    @Test
    void rejectNullInput() {
        assertThatThrownBy(
                () -> new ScrumPoker().identifyExtremes(null),
                "should not be null"
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectEmptyList(){
        assertThatThrownBy(
                () -> new ScrumPoker().identifyExtremes(Collections.emptyList()),
                "should not be empty"
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectSingleEstimate() {
        assertThatThrownBy(
                () -> new ScrumPoker().identifyExtremes(
                        Collections.singletonList(
                                new Estimate("Developer", 1)
                        )
                ),
                "should not be empty"
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void twoEstimates() {
        List<Estimate> list = Arrays.asList(
                new Estimate("Dev 1", 10),
                new Estimate("Dev 2", 5)
        );

        List<String> devs = new ScrumPoker().identifyExtremes(list);

        assertThat(devs).containsExactlyInAnyOrder("Dev 1", "Dev 2");
    }

    @Test
    void manyEstimates() {
        List<Estimate> list = Arrays.asList(
                new Estimate("Dev 1", 10),
                new Estimate("Dev 2", 5),
                new Estimate("Dev 3", 2)
        );

        List<String> devs = new ScrumPoker().identifyExtremes(list);

        assertThat(devs).containsExactlyInAnyOrder("Dev 1", "Dev 3");
    }

    @Property
    void estimatesInAnyOrder(@ForAll("estimates") List<Estimate> estimates) {

        estimates.add(new Estimate("Mr lowest", 1));
        estimates.add(new Estimate("Mr highest", 100));
        Collections.shuffle(estimates);

        List<String> dev = new ScrumPoker().identifyExtremes(estimates);

        assertThat(dev)
                .containsExactlyInAnyOrder("Mr lowest", "Mr highest");
    }

    @Provide
    Arbitrary<List<Estimate>> estimates() {
        Arbitrary<String> names = Arbitraries.strings().withCharRange('a', 'z').ofLength(5);
        Arbitrary<Integer> values = Arbitraries.integers().between(2, 99);

        Arbitrary<Estimate> estimates = Combinators.combine(names, values)
                .as((name, value) -> new Estimate(name, value));

        return estimates.list().ofMinSize(1);
    }

    @Test
    void developersWithSameEstimates() {
        List<Estimate> list = Arrays.asList(
                new Estimate("Mauricio", 10),
                new Estimate("Arie", 5),
                new Estimate("Andy", 10),
                new Estimate("Frank", 7),
                new Estimate("Annibale", 5)
        );

        List<String> devs = new ScrumPoker().identifyExtremes(list);

        assertThat(devs)
                .containsExactlyInAnyOrder("Mauricio", "Arie");
    }

    @Test
    void allDevelopersWithTheSameEstimate() {
        List<Estimate> list = Arrays.asList(
                new Estimate("Mauricio", 10),
                new Estimate("Arie", 10),
                new Estimate("Andy", 10),
                new Estimate("Frank", 10),
                new Estimate("Annibale", 10)
        );
        List<String> devs = new ScrumPoker().identifyExtremes(list);

        assertThat(devs).isEmpty();

    }

}
