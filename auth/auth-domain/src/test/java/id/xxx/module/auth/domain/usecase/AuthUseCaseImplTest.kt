package id.xxx.module.auth.domain.usecase

import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.model.UserData
import id.xxx.module.auth.repository.AuthRepository
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.usecase.AuthUseCaseImpl
import id.xxx.module.common.Resources
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

internal class AuthUseCaseImplTest {

    private val repo: AuthRepository = Mockito.mock(AuthRepository::class.java)
    private val useCase: AuthUseCase = getAuthUseCase(repo)

    private fun getAuthUseCase(repo: AuthRepository): AuthUseCase {
        val constructor =
            AuthUseCaseImpl::class.java.getDeclaredConstructor(AuthRepository::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(repo) as AuthUseCase
    }

    @Test
    fun getCurrentUserSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        Mockito.`when`(repo.currentUser()).thenReturn(result)

        val currentUser = useCase.currentUser()
        val loading = currentUser.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = currentUser.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun getCurrentUserFailureTest() = runBlocking {
        val throwable = Throwable("error")
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Failure(throwable))
        }
        Mockito.`when`(repo.currentUser()).thenReturn(result)

        val currentUser = useCase.currentUser()
        val loading = currentUser.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val failure = currentUser.lastOrNull()
        Assert.assertTrue("Resources Failure", failure is Resources.Failure)
        Assert.assertTrue("Data", throwable == (failure as Resources.Failure).value)
    }

    @Test
    fun signInCostumeTokenSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        val type = SignInType.CostumeToken("token")
        Mockito.`when`(repo.signIn(type)).thenReturn(result)
        val signIn = useCase.signIn(type)
        val loading = signIn.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = signIn.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun signInPasswordSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        val type = SignInType.Password(email = "xxx@gmail.com", password = "password")
        Mockito.`when`(repo.signIn(type)).thenReturn(result)
        val signIn = useCase.signIn(type)
        val loading = signIn.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = signIn.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun signInPhoneSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        val type = SignInType.Phone("session_info", otp = "otp")
        Mockito.`when`(repo.signIn(type)).thenReturn(result)
        val signIn = useCase.signIn(type)
        val loading = signIn.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = signIn.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun signUpPasswordSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        val type = SignUpType.Password(
            password = "password", UserData(email = "xxx@gmail.com", "+628")
        )
        Mockito.`when`(repo.signUp(type)).thenReturn(result)
        val signIn = useCase.signUp(type)
        val loading = signIn.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = signIn.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun signUpPhoneSuccessTest() = runBlocking {
        val user = getMockUser()
        val result = flow {
            emit(Resources.Loading())
            delay(100)
            emit(Resources.Success(user))
        }
        val type = SignUpType.Phone(
            sessionInfo = "123456789",
            otp = "12345",
            data = UserData(email = "xxx@gmail.com", "+628")
        )
        Mockito.`when`(repo.signUp(type)).thenReturn(result)
        val signIn = useCase.signUp(type)
        val loading = signIn.firstOrNull()
        Assert.assertTrue("Resources Loading", loading is Resources.Loading)
        val success = signIn.lastOrNull()
        Assert.assertTrue("Resources Success", success is Resources.Success)
        Assert.assertTrue("Data", user == (success as Resources.Success).value)
    }

    @Test
    fun sendVerificationCodeTest() {

    }

    @Test
    fun signOutTest() {

    }

    @Test
    fun sendOobCodeTest() {

    }

    @Test
    fun resetPasswordTest() {

    }

    @Test
    fun updateTest() {

    }

    private fun getMockUser() = User(
        uid = "uid:test"
    )
}