/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.pmd;

import guru.nidi.codeassert.util.ResultMatcher;
import net.sourceforge.pmd.cpd.Mark;
import net.sourceforge.pmd.cpd.Match;
import org.hamcrest.Description;

import java.util.*;

import static java.lang.System.lineSeparator;

public class CpdMatcher extends ResultMatcher<CpdResult, Match> {
    private static final Comparator<Mark> MARK_COMPARATOR = Comparator.comparing(Mark::getFilename);

    public void describeTo(Description description) {
        description.appendText("Has no code duplications");
    }

    @Override
    protected void describeMismatchSafely(CpdResult item, Description description) {
        for (final Match match : item.findings()) {
            description.appendText(lineSeparator()).appendText(printMatch(match));
        }
    }

    private String printMatch(Match match) {
        final StringBuilder s = new StringBuilder();
        boolean first = true;
        for (final Mark mark : getMarks(match)) {
            s.append(first ? String.format("%-4d ", match.getTokenCount()) : "     ");
            first = false;
            s.append(String.format("%s:%d-%d%n", mark.getFilename(), mark.getBeginLine(), mark.getEndLine()));
        }
        return s.substring(0, s.length() - lineSeparator().length());
    }

    private List<Mark> getMarks(Match match) {
        final List<Mark> marks = new ArrayList<>();
        final Iterator<Mark> iter = match.iterator();
        while (iter.hasNext()) {
            marks.add(iter.next());
        }
        marks.sort(MARK_COMPARATOR);
        return marks;
    }
}
