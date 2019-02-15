/*
 * Copyright (C) 2019 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epam.reportportal.jbehave;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

import org.jbehave.core.model.ExamplesTable;
import org.junit.After;
import org.junit.Test;

import io.reactivex.Maybe;

/**
 * @author Valery Yatsynovich
 */
public class JBehaveContextStoryTest {

    private final JBehaveContext.Story story = new JBehaveContext.Story();

    @After
    public void tearDown() {
        story.setCurrentStep(null);
        story.setCurrentStep(null);
        story.setCurrentScenario(null);
        story.setCurrentStoryId(null);
    }

    @Test
    public void testStoryHasNoExamplesWhenExamplesTableIsEmpty() {
        story.setExamples(new JBehaveContext.Examples(Collections.<String>emptyList(), ExamplesTable.EMPTY));
        assertFalse(story.hasExamples());
    }

    @Test
    public void testItemsCache() {
        String storyId = "storyId";
        String scenario = "scenario";
        String compositeStep = "composite step";
        String composedStep = "composed step";
        story.setCurrentStoryId(Maybe.just(storyId));
        story.setCurrentScenario(Maybe.just(scenario));
        story.setCurrentStep(Maybe.just(compositeStep));
        story.setCurrentStep(Maybe.just(composedStep));
        Deque<Maybe<String>> itemsCache = JBehaveContext.getItemsCache();
        assertEquals(4, itemsCache.size());
        Iterator<Maybe<String>> iterator = itemsCache.iterator();
        assertEquals(composedStep, iterator.next().blockingGet());
        assertEquals(compositeStep, iterator.next().blockingGet());
        assertEquals(scenario, iterator.next().blockingGet());
        assertEquals(storyId, iterator.next().blockingGet());
    }

    @Test
    public void testFullStoryExecution() {
        story.setCurrentStoryId(Maybe.just("storyId"));
        story.setCurrentScenario(Maybe.just("scenario"));
        story.setCurrentStep(Maybe.just("composite step"));
        story.setCurrentStep(Maybe.just("composed step"));
        story.setCurrentStep(null);
        story.setCurrentStep(null);
        story.setCurrentScenario(null);
        story.setCurrentStoryId(null);
        assertEquals(0, JBehaveContext.getItemsCache().size());
    }
}
