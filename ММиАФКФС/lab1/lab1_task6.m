clear; clc; close all;
% x = [Tf, T0, Tv, tau_m, tau_y, Tm, Ty], hours
x = [100 1100 5 720 8640 20 200];
names = {'Tf','T0','Tv','tau_m','tau_y','Tm','Ty'};
[Png,p,Q] = model(x);
disp(table((1:7)',p,'VariableNames',{'State','p'}));
fprintf('P_ready = %.9f\nP_not_ready = %.9f\n', ...
    1-Png,Png);
assert(abs(sum(p)-1) < 1e-10);
assert(all(p >= -1e-12));
assert(norm(Q'*p,inf) < 1e-10);

R = zeros(7,3);
h = 1e-5;
for j = 1:7
    xm = x; xp = x;
    xm(j) = 0.9*x(j); xp(j) = 1.1*x(j);
    R(j,1) = model(xm); R(j,2) = model(xp);
    xm(j) = (1-h)*x(j); xp(j) = (1+h)*x(j);
    R(j,3) = (model(xp)-model(xm))/(2*h*Png);
end
disp(table(names',R(:,1),R(:,2),R(:,3), ...
    'VariableNames',{'Parameter','Minus10','Plus10','E'}));
[~,jmax] = max(abs(R(:,3)));
fprintf('Most influential: %s\n',names{jmax});

k = linspace(0.5,1.5,151);
Y = zeros(numel(k),7);
for j = 1:7
    for n = 1:numel(k)
        z = x; z(j) = x(j)*k(n);
        Y(n,j) = model(z);
    end
end
figure('Color','w');
plot(k,100*Y,'LineWidth',1.5); grid on;
xlabel('Parameter / baseline');
ylabel('Unavailability, %');
legend(names,'Location','best');
figure('Color','w');
bar(abs(R(:,3))); grid on;
set(gca,'XTick',1:7,'XTickLabel',names);
ylabel('Absolute relative sensitivity');
clear; clc; close all;
% x = [Tf, T0, Tv, tau_m, tau_y, Tm, Ty], hours
x = [100 1100 5 720 8640 20 200];
names = {'Tf','T0','Tv','tau_m','tau_y','Tm','Ty'};
[Png,p,Q] = model(x);
disp(table((1:7)',p,'VariableNames',{'State','p'}));
fprintf('P_ready = %.9f\nP_not_ready = %.9f\n', ...
    1-Png,Png);
assert(abs(sum(p)-1) < 1e-10);
assert(all(p >= -1e-12));
assert(norm(Q'*p,inf) < 1e-10);

R = zeros(7,3);
h = 1e-5;
for j = 1:7
    xm = x; xp = x;
    xm(j) = 0.9*x(j); xp(j) = 1.1*x(j);
    R(j,1) = model(xm); R(j,2) = model(xp);
    xm(j) = (1-h)*x(j); xp(j) = (1+h)*x(j);
    R(j,3) = (model(xp)-model(xm))/(2*h*Png);
end
disp(table(names',R(:,1),R(:,2),R(:,3), ...
    'VariableNames',{'Parameter','Minus10','Plus10','E'}));
[~,jmax] = max(abs(R(:,3)));
fprintf('Most influential: %s\n',names{jmax});

k = linspace(0.5,1.5,151);
Y = zeros(numel(k),7);
for j = 1:7
    for n = 1:numel(k)
        z = x; z(j) = x(j)*k(n);
        Y(n,j) = model(z);
    end
end
figure('Color','w');
plot(k,100*Y,'LineWidth',1.5); grid on;
xlabel('Parameter / baseline');
ylabel('Unavailability, %');
legend(names,'Location','best');
figure('Color','w');
bar(abs(R(:,3))); grid on;
set(gca,'XTick',1:7,'XTickLabel',names);
ylabel('Absolute relative sensitivity');

function [Png,p,Q] = model(x)
    Tf=x(1); T0=x(2); Tv=x(3);
    tm=x(4); ty=x(5); Tm=x(6); Ty=x(7);
    F = @(t) 1/(T0-t/expm1(t/T0));
    l12=1/T0; l15=1/tm; l16=1/ty;
    l23=F(ty); l24=F(tm);
    l37=1/Ty; l42=1/Tm; l51=1/Tm;
    l61=1/Ty; l67=1/Ty; l71=1/Tv;

    Q=zeros(7);
    Q(1,2)=l12; Q(1,5)=l15; Q(1,6)=l16;
    Q(2,3)=l23; Q(2,4)=l24;
    Q(3,7)=l37; Q(4,2)=l42; Q(5,1)=l51;
    Q(6,1)=l61; Q(6,7)=l67; Q(7,1)=l71;
    Q=Q-diag(sum(Q,2));

    A=Q'; b=zeros(7,1);
    A(7,:)=1; b(7)=1;
    p=A\b;
    Png=1-p(1)-p(5);
end
