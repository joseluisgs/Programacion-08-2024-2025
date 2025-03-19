package di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication
import org.koin.fileProperties
import org.koin.ksp.generated.defaultModule
import org.koin.test.check.ParametersBinding
import org.koin.test.junit5.AutoCloseKoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class ModuleVerificationTest : AutoCloseKoinTest() {
    
    @Test
    fun verifyModules() {
        koinApplication {
            fileProperties("/config.properties")
            defaultModule.verify(
                extraTypes = listOf(
                    Boolean::class,
                    Int::class
                )
            )
        }
    }
    
    @Test
    fun checkModules() {
        koinApplication {
            fileProperties("/config.properties")
            modules(defaultModule)
            fun ParametersBinding.() {
                defaultModule
            }
        }
    }
}