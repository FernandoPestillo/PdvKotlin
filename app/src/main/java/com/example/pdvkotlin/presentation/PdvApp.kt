package com.example.pdvkotlin.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pdvkotlin.core.navigation.PdvRoute
import com.example.pdvkotlin.core.ui.toMoney
import com.example.pdvkotlin.domain.model.CartItem
import com.example.pdvkotlin.domain.model.Product
import com.example.pdvkotlin.features.auth.AuthViewModel
import com.example.pdvkotlin.features.cart.CartState
import com.example.pdvkotlin.features.payment.PaymentViewModel
import com.example.pdvkotlin.features.payment.ReceiptStore
import com.example.pdvkotlin.features.products.ProductsViewModel
import com.example.pdvkotlin.features.reports.ReportsViewModel
import com.example.pdvkotlin.features.sales.SaleViewModel
import com.example.pdvkotlin.features.settings.SettingsViewModel
import com.example.pdvkotlin.ui.theme.PdvKotlinTheme
import kotlinx.coroutines.delay

@Composable
fun PdvApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PdvRoute.Splash.route) {
        composable(PdvRoute.Splash.route) { SplashScreen(navController) }
        composable(PdvRoute.Login.route) { LoginScreen(navController) }
        composable(PdvRoute.Dashboard.route) { DashboardScreen(navController) }
        composable(PdvRoute.Products.route) { ProductsScreen(navController) }
        composable(PdvRoute.Sale.route) { SaleScreen(navController) }
        composable(PdvRoute.Cart.route) { CartScreen(navController) }
        composable(PdvRoute.Payment.route) { PaymentScreen(navController) }
        composable(PdvRoute.Receipt.route) { ReceiptScreen(navController) }
        composable(PdvRoute.Reports.route) { ReportsScreen(navController) }
        composable(PdvRoute.Settings.route) { SettingsScreen(navController) }
    }
}

@Composable
private fun SplashScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState()
    LaunchedEffect(user) {
        delay(700)
        navController.navigate(if (user == null) PdvRoute.Login.route else PdvRoute.Dashboard.route) {
            popUpTo(PdvRoute.Splash.route) { inclusive = true }
        }
    }
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.PointOfSale, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(72.dp))
            Text("PDV Kotlin", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Venda rapida, offline e escalavel", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun LoginScreen(navController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    val state by viewModel.loginState.collectAsState()
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Icon(Icons.Default.PointOfSale, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
                Text("Entrar no PDV", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                OutlinedTextField(state.username, viewModel::setUsername, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(state.password, viewModel::setPassword, label = { Text("Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                AnimatedVisibility(state.error != null) { Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error) }
                Button(
                    onClick = { viewModel.login { navController.navigateRoot(PdvRoute.Dashboard.route) } },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = !state.loading,
                ) {
                    if (state.loading) CircularProgressIndicator(Modifier.size(22.dp)) else Text("Entrar")
                }
                Text("Usuarios mock: admin/admin123, caixa/caixa123, gerente/gerente123", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DashboardScreen(navController: NavHostController) {
    PdvScaffold("Dashboard", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Operacao de hoje", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DashboardCard("Nova venda", "Abrir caixa", Icons.Default.PointOfSale) { navController.navigate(PdvRoute.Sale.route) }
                DashboardCard("Produtos", "Cadastro e estoque", Icons.Default.Inventory2) { navController.navigate(PdvRoute.Products.route) }
                DashboardCard("Relatorios", "Vendas e fechamento", Icons.Default.Assessment) { navController.navigate(PdvRoute.Reports.route) }
                DashboardCard("Configuracoes", "Empresa e impressora", Icons.Default.Settings) { navController.navigate(PdvRoute.Settings.route) }
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Pronto para crescer", fontWeight = FontWeight.Bold)
                    Text("Room local, fila pendingSync, API Retrofit fake e pontos preparados para scanner, Bluetooth, PDF e fiscal SAT/NFC-e.")
                }
            }
        }
    }
}

@Composable
private fun ProductsScreen(navController: NavHostController, viewModel: ProductsViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val query by viewModel.query.collectAsState()
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    PdvScaffold("Produtos", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(query, viewModel::setQuery, label = { Text("Busca rapida por nome, categoria ou codigo") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("Novo produto") }, modifier = Modifier.weight(1f))
                OutlinedTextField(price, { price = it }, label = { Text("Preco") }, modifier = Modifier.width(120.dp))
                IconButton(onClick = { viewModel.addQuickProduct(name, price); name = ""; price = "" }) { Icon(Icons.Default.Add, contentDescription = "Adicionar") }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(products) { ProductRow(it, onAdd = {}) }
            }
        }
    }
}

@Composable
private fun SaleScreen(navController: NavHostController, viewModel: SaleViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val query by viewModel.query.collectAsState()
    PdvScaffold("Caixa", navController) {
        Row(Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(query, viewModel::setQuery, label = { Text("Buscar ou bipar codigo de barras") }, trailingIcon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) }, modifier = Modifier.fillMaxWidth())
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(products) { ProductRow(it, onAdd = { viewModel.add(it) }) }
                }
            }
            CartPanel(cart, viewModel::changeQuantity, viewModel::remove, viewModel::setDiscount, viewModel::setNotes, Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = viewModel::holdSale, modifier = Modifier.weight(1f)) { Text("Segurar") }
                    Button(onClick = { navController.navigate(PdvRoute.Payment.route) }, modifier = Modifier.weight(1f), enabled = cart.items.isNotEmpty()) { Text("Pagar") }
                }
                OutlinedButton(onClick = viewModel::cancel, modifier = Modifier.fillMaxWidth()) { Text("Cancelar venda") }
            }
        }
    }
}

@Composable
private fun CartScreen(navController: NavHostController, viewModel: SaleViewModel = hiltViewModel()) {
    val cart by viewModel.cart.collectAsState()
    PdvScaffold("Carrinho", navController) {
        CartPanel(cart, viewModel::changeQuantity, viewModel::remove, viewModel::setDiscount, viewModel::setNotes, Modifier.padding(16.dp)) {
            Button(onClick = { navController.navigate(PdvRoute.Payment.route) }, modifier = Modifier.fillMaxWidth(), enabled = cart.items.isNotEmpty()) { Text("Ir para pagamento") }
        }
    }
}

@Composable
private fun PaymentScreen(navController: NavHostController, viewModel: PaymentViewModel = hiltViewModel()) {
    val cart by viewModel.cart.collectAsState()
    val state by viewModel.state.collectAsState()
    val paid = listOf(state.cash, state.credit, state.debit, state.pix).sumOf { it.replace(",", ".").toDoubleOrNull() ?: 0.0 }
    PdvScaffold("Pagamento", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryLine("Total", cart.total.toMoney(), strong = true)
            SummaryLine("Recebido", paid.toMoney())
            SummaryLine("Troco", (paid - cart.total).coerceAtLeast(0.0).toMoney())
            PaymentField("Dinheiro", state.cash, viewModel::setCash)
            PaymentField("Cartao credito", state.credit, viewModel::setCredit)
            PaymentField("Cartao debito", state.debit, viewModel::setDebit)
            PaymentField("PIX", state.pix, viewModel::setPix)
            Button(onClick = { viewModel.finishSale { navController.navigateRoot(PdvRoute.Receipt.route) } }, enabled = paid >= cart.total && cart.items.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Icon(Icons.Default.CreditCard, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Finalizar venda")
            }
        }
    }
}

@Composable
private fun ReceiptScreen(navController: NavHostController, viewModel: ReceiptViewModel = hiltViewModel()) {
    val sale by viewModel.store.sale.collectAsState()
    PdvScaffold("Recibo", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
            Text("Comprovante", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            sale?.items.orEmpty().forEach { SummaryLine("${it.quantity}x ${it.product.name}", it.total.toMoney()) }
            SummaryLine("Total", sale?.total?.toMoney() ?: "R$ 0,00", strong = true)
            Text("Fiscal: pronto para integracao SAT/NFC-e. Impressao Bluetooth e compartilhamento PDF previstos nos pontos de extensao.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Icon(Icons.Default.Print, null); Spacer(Modifier.width(6.dp)); Text("Imprimir") }
                Button(onClick = { navController.navigateRoot(PdvRoute.Dashboard.route) }, modifier = Modifier.weight(1f)) { Text("Concluir") }
            }
        }
    }
}

@dagger.hilt.android.lifecycle.HiltViewModel
class ReceiptViewModel @javax.inject.Inject constructor(val store: ReceiptStore) : androidx.lifecycle.ViewModel()

@Composable
private fun ReportsScreen(navController: NavHostController, viewModel: ReportsViewModel = hiltViewModel()) {
    val summary by viewModel.summary.collectAsState()
    PdvScaffold("Relatorios", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Vendas do dia", summary.dailySalesTotal.toMoney())
            SummaryCard("Fechamento de caixa", "${summary.salesCount} vendas")
            SummaryCard("Ticket medio", summary.averageTicket.toMoney())
            Text("Produtos mais vendidos", fontWeight = FontWeight.Bold)
            summary.bestSellers.ifEmpty { listOf("Sem vendas registradas" to 0) }.forEach { Text("${it.first}  ${it.second} un.") }
        }
    }
}

@Composable
private fun SettingsScreen(navController: NavHostController, viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    PdvScaffold("Configuracoes", navController) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Tema escuro")
                Switch(state.darkMode, viewModel::setDarkMode)
            }
            SummaryCard("Empresa", state.companyName)
            SummaryCard("Impressora Bluetooth", state.printerName)
            SummaryCard("Taxas e impostos", "${state.taxRate}%")
            SummaryCard("Backup local", "Banco Room com Auto Backup Android ativo")
            OutlinedButton(onClick = { viewModel.logout { navController.navigateRoot(PdvRoute.Login.route) } }, modifier = Modifier.fillMaxWidth()) {
                Text("Sair da sessao")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PdvScaffold(title: String, navController: NavHostController, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) }, actions = {
                IconButton({ navController.navigate(PdvRoute.Dashboard.route) }) { Icon(Icons.Default.Dashboard, "Dashboard") }
                IconButton({ navController.navigate(PdvRoute.Sale.route) }) { Icon(Icons.Default.PointOfSale, "Caixa") }
                IconButton({ navController.navigate(PdvRoute.Cart.route) }) { Icon(Icons.Default.Receipt, "Carrinho") }
                IconButton({ navController.navigate(PdvRoute.Settings.route) }) { Icon(Icons.Default.Settings, "Configuracoes") }
            })
        },
        content = { padding -> Box(Modifier.padding(padding)) { content() } },
    )
}

@Composable
private fun DashboardCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(modifier = Modifier.width(170.dp).height(128.dp).clickable(onClick = onClick), shape = RoundedCornerShape(8.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product, onAdd: (Product) -> Unit) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("${product.category}  ${product.barcode}  Estoque: ${product.stock}", style = MaterialTheme.typography.bodySmall)
            }
            Text(product.salePrice.toMoney(), fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { onAdd(product) }) { Icon(Icons.Default.Add, contentDescription = "Adicionar") }
        }
    }
}

@Composable
private fun CartPanel(
    cart: CartState,
    onQuantity: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    onDiscount: (String) -> Unit,
    onNotes: (String) -> Unit,
    modifier: Modifier = Modifier,
    footer: @Composable () -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Carrinho", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(cart.items) { item -> CartItemRow(item, onQuantity, onRemove) }
        }
        OutlinedTextField(cart.totalDiscount.takeIf { it > 0 }?.toString().orEmpty(), onDiscount, label = { Text("Desconto total") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(cart.notes, onNotes, label = { Text("Observacoes") }, modifier = Modifier.fillMaxWidth())
        SummaryLine("Subtotal", cart.subtotal.toMoney())
        SummaryLine("Total", cart.total.toMoney(), strong = true)
        footer()
    }
}

@Composable
private fun CartItemRow(item: CartItem, onQuantity: (String, Int) -> Unit, onRemove: (String) -> Unit) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(item.product.name, fontWeight = FontWeight.Bold)
                Text(item.total.toMoney(), style = MaterialTheme.typography.bodySmall)
            }
            IconButton({ onQuantity(item.product.id, item.quantity - 1) }) { Icon(Icons.Default.Remove, "Diminuir") }
            Text(item.quantity.toString(), fontWeight = FontWeight.Bold)
            IconButton({ onQuantity(item.product.id, item.quantity + 1) }) { Icon(Icons.Default.Add, "Aumentar") }
            IconButton({ onRemove(item.product.id) }) { Icon(Icons.Default.Delete, "Remover") }
        }
    }
}

@Composable
private fun PaymentField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value, onChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun SummaryLine(label: String, value: String, strong: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun SummaryCard(title: String, value: String) {
    Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

private fun NavHostController.navigateRoot(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

@Preview(showBackground = true, widthDp = 900)
@Composable
private fun DashboardPreview() {
    PdvKotlinTheme {
        DashboardScreen(rememberNavController())
    }
}
