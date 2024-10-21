package id.xxx.module.auth.data

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import id.xxx.module.auth.data.source.local.LocalDataSource
import id.xxx.module.auth.data.source.local.db.AppDatabase
import id.xxx.module.auth.data.source.remote.RemoteDataSource
import id.xxx.module.auth.data.source.remote.helpers.MyFirebase
import id.xxx.module.auth.domain.exception.AuthInvalidCredentialsException
import id.xxx.module.auth.domain.exception.AuthUserCollisionException
import id.xxx.module.auth.domain.model.TypeSignIn
import id.xxx.module.auth.domain.model.TypeSignUp
import id.xxx.module.common.Resources
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var context: Context

    @Before
    fun before() {
        val appContext =
            InstrumentationRegistry.getInstrumentation().targetContext
        context = appContext
        MyFirebase.firebaseInit(appContext)
    }

    @Test
    fun sign_up_type_password_already_test() = runBlocking {
//        val type = TypeSignUp.Password(getRandomEmail(), "123456")
//        val firebaseAuth = MyFirebase.getFirebaseAuth()
//        try {
//            firebaseAuth.createUserWithEmailAndPassword(
//                type.email, type.password
//            ).await()
//        } catch (_: Throwable) {
//            /**/
//        }
//
//        val db = AppDatabase.getInstance(context)
//        val dao = db.userDao
//        val local = LocalDataSource.getInstance(dao = dao)
//        val remote = RemoteDataSource.getInstance()
//        val repo = AuthRepositoryImpl
//            .getInstance(local = local, remote = remote)
//        val auth = repo.signUp(type)
//        firebaseAuth.currentUser?.delete()
//        Assert.assertEquals(Resources.Loading(), auth.firstOrNull())
//        val err = auth.lastOrNull() as Resources.Failure
//        Assert.assertTrue(err.value is AuthUserCollisionException)
    }

    @Test
    fun sign_in_type_password_invalid_email_or_password_test() = runBlocking {
//        val db = AppDatabase.getInstance(context)
//        val dao = db.userDao
//        val local = LocalDataSource.getInstance(dao = dao)
//        val remote = RemoteDataSource.getInstance()
//        val repo = AuthRepositoryImpl
//            .getInstance(local = local, remote = remote)
//        val type = TypeSignIn.Password(getRandomEmail(), "892198218")
//        val auth = repo.signIn(type = type)
//        Assert.assertEquals(Resources.Loading(), auth.firstOrNull())
//        val err = auth.lastOrNull() as Resources.Failure
//        Assert.assertTrue(err.value is AuthInvalidCredentialsException)
    }

    @Test
    fun get_current_user_test() = runBlocking {
//        val db = AppDatabase.getInstance(context)
//        val dao = db.userDao
//        val local = LocalDataSource.getInstance(dao = dao)
//        val remote = RemoteDataSource.getInstance()
//        val repo = AuthRepositoryImpl
//            .getInstance(local = local, remote = remote)
//        repo.getCurrentUser().collect {
//            println(it)
//            if (it is Resources.Success) {
//                if (it.value == null) {
//                    repo.signIn(TypeSignIn.Password("a@gmail.com", "123456")).lastOrNull()
//                } else {
//                    cancel()
//                }
//            }
//        }
    }

    fun getRandomEmail(length: Int = 15): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return "${(1..length).map { allowedChars.random() }.joinToString("")}@gmail.com"
    }
}