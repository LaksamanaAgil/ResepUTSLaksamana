package com.laksamana.gohalalappresep

import androidx.compose.material3.Card
import androidx.compose.material3.TopAppBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.shadow
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.laksamana.gohalalappresep.data.Resep
import com.laksamana.gohalalappresep.data.strawberryCake
import com.laksamana.gohalalappresep.ui.AppBarCollapsedHeight
import com.laksamana.gohalalappresep.ui.AppBarExpendedHeight
import com.laksamana.gohalalappresep.ui.theme.*
import kotlin.math.max
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoHalalAppResepTheme {
                ProvideWindowInsets {
                    Surface(color = White) {
                        MainFragment(strawberryCake) //TODO: Nemanggil main fragment dengan value dari data class resep "strawberryCake"
                    }
                }
            }
        }
    }
}
//TODO: Mainfragment untuk memanggil data class Resep.kt, yang informasinya akan digunakan di function-function lain, serta untuk mengingat scroll state
@Composable
fun MainFragment(recipe: Resep) {
    val scrollState = rememberLazyListState()

    var thumbnail by remember {
        mutableStateOf(R.drawable.strawberry_pie_1)
    }

    Box {
        Content(recipe, scrollState)
        ParallaxToolbar(recipe, scrollState, thumbnail = thumbnail)
    }
}
//TODO: Membuat top app bar yang mengandung gambar utama resep, yang menghilang setelah scrolldown, serta judul resep dan ditambahkan tombol nonaktif untuk nilai eestetika
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParallaxToolbar(recipe: Resep , scrollState: LazyListState, @DrawableRes thumbnail: Int) {
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight
    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - LocalWindowInsets.current.systemBars.layoutInsets.top
    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset



    TopAppBar(
        modifier = Modifier
            .height(
                AppBarExpendedHeight
            )
            .background(White)
            .padding()
            .offset { IntOffset(x = 0, y = -offset) }
            .shadow(
                elevation = if (offset==maxOffset)4.dp else 0.dp
            ),
        title = {
            Text("Tes")
        }
    )
        Column {
            Box(
                Modifier
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    }) {
                Image(
                    painter = painterResource(thumbnail),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.4f, Transparent),
                                    Pair(1f, White)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        recipe.category,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(Shapes.small)
                            .background(LightGray)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    )
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    recipe.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offsetProgress).dp)
                        .scale(1f - 0.25f * offsetProgress)
                )

            }
        }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp)
    ) {
        CircularButton(R.drawable.ic_arrow_back)
        CircularButton(R.drawable.ic_favorite)
    }
}


@Composable
fun CircularButton( //TODO: Function ayang dapat dipanggil untuk membuat tombol melingkar, misal untuk penambahan dan pengurangan porsi, serta tombol kosmetik untuk tombol favorit dan tombol panah kembali
    @DrawableRes iconResouce: Int,
    color: Color = Gray,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Gray
        ),
        elevation = ButtonDefaults.buttonElevation(),
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(painterResource(id = iconResouce), null)
    }
}

@Composable
fun Content(recipe: Resep, scrollState: LazyListState) { //TODO: Memanggil value didalam data class, serta fungsi-fungsi didalam myActivity untuk membangun tampilan aplikasi
    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight), state = scrollState) {
        item {
            BasicInfo(recipe)
            Description(recipe)
            ServingCalculator()
            IngredientsHeader()
            IngredientsList(recipe)
            ShoppingListButton()
            Reviews(recipe)
            Images()
        }
    }
}

@Composable
fun Images() {
    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_2),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_3),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
    }
}

@Composable
fun Reviews(recipe: Resep) { //TODO: Function non-interaktif yang menunjukkan jumlah foto dan komentar, serta tombol kosmetik "Lihat semua"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Reviews", fontWeight = Bold)
            Text(recipe.reviews, color = DarkGray)
        }
        Button(
            onClick = { /*TODO*/ }, elevation = null, colors = ButtonDefaults.buttonColors(
                containerColor = Transparent, contentColor = Pink
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Lihat semua")
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ShoppingListButton() { //TODO: Function kosmetik/ hanya untuk tamilan dan tidak memiliki fungsi yang dapat berinteraksi dengan user
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = Color.Black
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Tambahkan ke keranjang", modifier = Modifier.padding(8.dp))
    }

}

@Composable
fun IngredientsList(recipe: Resep) { //TODO: Functio nuntuk memanggil gambar, judul dan berat bahan untuk digunakan dalam sebuah grid
    EasyGrid(nColumns = 3, items = recipe.ingredients) {
        IngredientCard(it.image, it.title, it.berat, Modifier)
    }
}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) { //TODO: function untuk menetapkan bentuk grid untuk kartu-kartu bahan/ingredients
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}
public var value by mutableStateOf(1) //TODO: Public variabel yang dipanggil untuk nilai default serving/ porsi

@Composable
fun IngredientCard( //TODO: Kartu bahan yang menunjukkan gambar serta berat bahan yang diperlukan
    @DrawableRes iconResource: Int,
    title: String,
    berat: Int,
    modifier: Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card (
            shape = Shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor= LightGray),
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
        val tes = value*berat //TODO: Value didalam column yang mengalami recomposition setiap nilai porsi berubah
        Text(text = title, modifier = Modifier.width(100.dp), fontSize = 14.sp, fontWeight = Medium)
        Text(text = "$tes gram", color = DarkGray, modifier = Modifier.width(100.dp), fontSize = 14.sp)
    }

}


@Composable
fun IngredientsHeader() { //TODO: Function untuk barisan yang mengandung tombol bahan dan tombol alat(nonaktif)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("Bahan", true, Modifier.weight(1f))
        TabButton("Alat", false, Modifier.weight(1f))

    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) { //TODO: Fungsi yang digunakan untuk tombol bahan, yang nanti akan dipanggil di function lain
    Button(
        onClick = { /*TODO*/ },
        shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors( //TODO: Untuk tombol aktif seperti tombol bahan
            containerColor = Pink,
            contentColor = White
        ) else ButtonDefaults.buttonColors( //TODO: Untuk tombol nonaktif/bersifat kosmetik
            containerColor = LightGray,
            contentColor = DarkGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun ServingCalculator() { //TODO: Untuk tombol jumlah porsi, sehingga dapat menyesuaikan jumlah bahan

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .padding(horizontal = 16.dp)
    ) {

        Text(text = "Porsi", Modifier.weight(1f), fontWeight = Medium)
        CircularButton(iconResouce = R.drawable.ic_minus, color = Pink) { value-- }
        Text(text = "$value", Modifier.padding(16.dp), fontWeight = Medium)
        CircularButton(iconResouce = R.drawable.ic_plus, color = Pink) { value++ }
    }
}


@Composable
fun Description(recipe: Resep) { //TODO: function untuk memanggil val deskripsi pendek dari public val strawberryCake yang ada di Resep.kt
    Text(
        text = recipe.description,
        fontWeight = Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun BasicInfo(recipe: Resep) { //TODO: Function yang mengandung row untukk tampilan waktu memasak, jumlah kalori, dan rating
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        InfoColumn(R.drawable.ic_clock, recipe.cookingTime)
        InfoColumn(R.drawable.ic_flame, recipe.energy)
        InfoColumn(R.drawable.ic_star, recipe.rating)
    }
}

@Composable
fun InfoColumn(@DrawableRes iconResouce: Int, text: String) { //TODO: function yang memberi warna pink pada ikon di kolom/row dari fun basicInfo
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResouce),
            contentDescription = null,
            tint = Pink,
            modifier = Modifier.height(24.dp)
        )
        Text(text = text, fontWeight = Bold)
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 1400)
@Composable
fun DefaultPreview() { //TODO: function untuk menunjukkan preview aplikasi di Android Studio
    GoHalalAppResepTheme {
        MainFragment(strawberryCake)
    }
}