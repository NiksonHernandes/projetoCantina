import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { Router } from '@angular/router';
import Endpoints from '../../core/sources/api.source';
import { environment } from 'src/environments/environments';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {

    private currentUserSubject: BehaviorSubject<Usuario | null>;
    public currentUserObservable: Observable<Usuario | null>;

    private accessTokenSubject: BehaviorSubject<string | null>;
    public accessTokenObservable: Observable<string | null>;

    apiUrl = environment.apiUrl;

    constructor(private router: Router, private http: HttpClient) {
        this.currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')!));
        this.currentUserObservable = this.currentUserSubject.asObservable();

        this.accessTokenSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('accessToken')!));
        this.accessTokenObservable = this.accessTokenSubject.asObservable();
    };

    public get currentUser(): Usuario | null {
        return this.currentUserSubject.value;
    };

    public get accessToken(): string | null {
        return this.accessTokenSubject.value;
    };

    login(username: string, senha: string): Observable<any> {
        console.log("inicip" + this.apiUrl + "/login")
        return this.http.post<any>(`${this.apiUrl}${Endpoints.auth.login}`, { username, senha })
            .pipe(map(ssoDto => {
                localStorage.setItem('currentUser', JSON.stringify(ssoDto.current_user));
                localStorage.setItem('accessToken', JSON.stringify(ssoDto.access_token));

                this.currentUserSubject.next(ssoDto.current_user);
                this.accessTokenSubject.next(ssoDto.access_token);

                return ssoDto;
            }));
    };

    logout(): void {
        localStorage.removeItem('currentUser');
        localStorage.removeItem('accessToken');

        this.currentUserSubject.next(null);
        this.accessTokenSubject.next(null);

        this.router.navigate(['/login']);
    };

}
