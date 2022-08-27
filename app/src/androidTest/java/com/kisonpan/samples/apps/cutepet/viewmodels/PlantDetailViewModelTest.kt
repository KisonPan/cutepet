/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kisonpan.samples.apps.cutepet.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kisonpan.samples.apps.cutepet.MainCoroutineRule
import com.kisonpan.samples.apps.cutepet.data.AppDatabase
import com.kisonpan.samples.apps.cutepet.data.GardenPlantingRepository
import com.kisonpan.samples.apps.cutepet.data.PlantRepository
import com.kisonpan.samples.apps.cutepet.runBlockingTest
import com.kisonpan.samples.apps.cutepet.utilities.getValue
import com.kisonpan.samples.apps.cutepet.utilities.testPlant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.jvm.Throws

@HiltAndroidTest
class PetDetailViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PetDetailViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = RuleChain
            .outerRule(hiltRule)
            .around(instantTaskExecutorRule)
            .around(coroutineRule)

    @Inject
    lateinit var plantRepository: PlantRepository

    @Inject
    lateinit var gardenPlantRepository: GardenPlantingRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
            set("plantId", testPlant.plantId)
        }
        viewModel = PetDetailViewModel(savedStateHandle, plantRepository, gardenPlantRepository)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest {
        assertFalse(getValue(viewModel.isPlanted))
    }
}
