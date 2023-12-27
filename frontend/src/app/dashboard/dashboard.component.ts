import { Component, OnInit } from '@angular/core';
import { ProductService } from './product.service';
import { StockMovementListService } from './stock-movement-list.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    totalProducts = 0;
    totalMovements = 0;

    constructor(
        private productService: ProductService,
        private stockMovementListService: StockMovementListService
    ) {}

    ngOnInit(): void {
        this.productService.getProducts().subscribe((products: any[]) => {
            this.totalProducts = products.length;
        });

        this.stockMovementListService.getMovements().subscribe((movements: any[]) => {
            this.totalMovements = movements.length;
        });
    }
}
