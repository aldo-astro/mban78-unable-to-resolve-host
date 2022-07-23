package com.example.retrofit500

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.retrofit500.ui.theme.Retrofit500Theme
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    companion object {
        const val BASE_URL = "https://iamnotavaliddomain.xyz"
    }

    private fun getChucker() =
        ChuckerInterceptor.Builder(this)
            .collector(ChuckerCollector(this))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

    private fun getOkHttp() = OkHttpClient.Builder()
        .addInterceptor(getChucker())
        .build()

    private fun getRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getOrderService() = getRetrofit(getOkHttp())
        .create(OrderService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Retrofit500Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen(getOrderService())
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    orderService: OrderService,
    viewModel: HomeViewModel = viewModel(initializer = {
        HomeViewModel(orderService)
    })
) {
    val uiState = viewModel.state
    Home(
        uiState,
        viewModel::onGetOrder200Click,
        viewModel::onGetOrder500Click,
        viewModel::onPostOrder500Click
    )
}

@Composable
fun rememberAppState(
    homeState: HomeState
) = remember(homeState) {
    homeState
}

@Composable
fun Home(
    homeState: HomeState = HomeState(),
    getOrder200ClickListener: () -> Unit = {},
    getOrder500ClickListener: () -> Unit = {},
    postOrder500ClickListener: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(homeState.text)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(12.dp)
        )
        CircularProgressIndicator()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(12.dp)
        )

        Button(onClick = { getOrder200ClickListener() }) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                text = "GET /order200"
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(12.dp)
        )
        Button(onClick = { getOrder500ClickListener() }) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                text = "GET /order500"
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(12.dp)
        )
        Button(onClick = { postOrder500ClickListener() }) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                text = "POST /order500"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Retrofit500Theme {
        Home(rememberAppState(HomeState()))
    }
}
