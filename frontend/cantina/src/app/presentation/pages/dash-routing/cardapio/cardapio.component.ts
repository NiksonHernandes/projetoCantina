import { Component, ElementRef } from '@angular/core';
import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-cardapio',
    templateUrl: './cardapio.component.html',
    styleUrls: ['./cardapio.component.scss']
})
export class CardapioComponent {
    constructor(config: NgbCarouselConfig, private el: ElementRef) {
        // customize default values of carousels used by this component tree
        config.interval = 10000;
        config.wrap = false;
        config.keyboard = false;
        config.pauseOnHover = false;
    }

    scrollToSection(sectionId: string): void {
        const element = this.el.nativeElement.querySelector(`#${sectionId}`);
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    }
}
