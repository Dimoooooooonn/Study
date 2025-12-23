clc; close all; clear all;
c=[ 3; 2; 0; 0; 0]
u=[16, 30, 21, 30, 16]
b=[21; 30; 16]
A=[3, 1, 1, 0, 0; 2, 2, 0, 1, 0; 0, 3, 0, 0, 1]
fprintf('Решение задачи и значение целевой функции \n')
[Xopt,Fval] = glpk(c,A,b,[],u,'SSS','CCCCC',-1)
fprintf('Решение задачи целочисленного программирования ∖n ')
[Xopt,Fval] = glpk(c,[A;eye(5)],[b;u'],[],[],'SSSUUUUU','IIIII',-1)
fprintf('Решение двойственной задачи\n')
[Yopt,Zval,extra,redcosts] = glpk([b;u'],[A',eye(5)],c,[-Inf(1,3),zeros(1,5)],[],'LLLLL','CCCCCCCC' )
fprintf('Решение целочисленной двойственной задачи\n')
[Yopt,Zval,extra,redcosts] = glpk([b;u'],[A',eye(5)],c,[-Inf(1,3),zeros(1,5)],[],'LLLLL','IIICCCCC')

