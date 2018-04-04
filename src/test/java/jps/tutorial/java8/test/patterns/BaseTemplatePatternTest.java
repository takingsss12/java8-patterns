package jps.tutorial.java8.test.patterns;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import org.testng.Assert;

import jps.tutorial.java8.test.DataUtils;
import jps.tutorial.java8.test.TestSupport;
import jsp.tutorial.java8.patterns.Candidate;
import jsp.tutorial.java8.patterns.template.EvaluationProcess;
import jsp.tutorial.java8.patterns.template.IEvaluationProcess;
import jsp.tutorial.java8.patterns.template.ProcessEvaluationAlgorithms;

/**
 * Basic functionality for Template pattern tests.
 *
 * @author John Psoroulas
 */
public class BaseTemplatePatternTest extends TestSupport {

  /**
   * Performs the evaluation process test for the specific {@link EvaluationProcess} type and the
   * respective special evaluation function
   * @param process the evaluation process
   * @param specialEvaluation the special evaluation function depended on the job type
   */
  protected void evaluationProcess(IEvaluationProcess process, ToIntFunction<Candidate> specialEvaluation) {
    /* Build the candidates (using <? extends Candidate> for read only access) */
    final List<? extends Candidate> candidates = DataUtils.buildCandidates(50);
    /* Calculate scores*/
    final Map<? extends Candidate, ? extends Integer> scores = calculateScores(candidates, process);

    /* Apply the common plus the special evaluation algorithm for each candidate and test it against the actual result */
    scores.forEach((candidate, score) -> Assert.assertEquals(score.intValue(), ProcessEvaluationAlgorithms.common(candidate) + specialEvaluation.applyAsInt(candidate), "Unexpected score"));
  }

  /**
   * Calculates the candidates scores
   * @param candidates The candidates
   * @param process The evaluation process
   * @return the scores, a mapping from the candidate to his score
   */
  private Map<Candidate, Integer> calculateScores (final List<? extends Candidate> candidates, IEvaluationProcess process)
  {
    return candidates.stream()
      .collect(Collectors.toMap(Function.identity(), process::evaluate));
  }

}
