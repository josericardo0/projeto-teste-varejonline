import { Component, OnInit } from '@angular/core';
import { ProductService } from './product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.css']
})
export class ProductManagementComponent implements OnInit {
  product: any = {};
  productId: any;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}


    ngOnInit(): void {
      this.loadProducts();
    }

  loadProducts(): void {
    this.productService.getProducts().subscribe((data: any[]) => {
      this.products = data;
    });
  }

  createProduct(): void {
    this.productService.createProduct(this.product).subscribe(() => {
      this.loadProducts();
    });
  }

  updateProduct(id: number): void {
    this.productService.updateProduct(id, this.product).subscribe(() => {
      this.loadProducts();
    });
  }

  deleteProduct(id: number): void {
    this.productService.deleteProduct(id).subscribe(() => {
      this.loadProducts();
    });
  }

  saveProduct(): void {
    this.productService.saveProduct(this.product).subscribe(
      (response: any) => {
        console.log('Produto salvo com sucesso!', response);
        this.product = {}; // Limpa os detalhes do produto após salvar
      },
      (error: any) => {
        console.error('Erro ao salvar o produto:', error);
      }
    );
  }
}
