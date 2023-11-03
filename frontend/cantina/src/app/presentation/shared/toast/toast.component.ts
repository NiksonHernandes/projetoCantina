import { Component, TemplateRef } from '@angular/core';
import { ToastService } from 'src/app/domain/services/toast.service';

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.scss']
})
export class ToastComponent {
  constructor(public toastService: ToastService) { }

  isTemplate(toast: { textOrTpl: any; }) {
    return toast.textOrTpl instanceof TemplateRef;
  }
}
