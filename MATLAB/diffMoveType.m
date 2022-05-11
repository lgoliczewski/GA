T1 = readtable('TSwithDifferentMoveTypes.csv');
% T2 = readtable('ftv47L60.csv');
% T3 = readtable('ftv47L90.csv');
% T4 = readtable('ftv47L120.csv');
% T5 = readtable('ftv47L150.csv');
% T6 = readtable('ftv47L180.csv');
% T7 = readtable('ftv47L210.csv');
% T8 = readtable('ftv47L240.csv');
% T9 = readtable('ftv47L270.csv');
% T10 = readtable('ftv47L300.csv');


K1 = table2array(T1);
% K2 = table2array(T2);
% K3 = table2array(T3);
% K4 = table2array(T4);
% K5 = table2array(T5);
% K6 = table2array(T6);
% K7 = table2array(T7);
% K8 = table2array(T8);
% K9 = table2array(T9);
% K10 = table2array(T10);

x1 = K1(:,1);
y1 = K1(:,2);
y2 = K1(:,3);
y3 = K1(:,4);


P = plot(x1,y1,x1,y2,x1,y3);
xlabel('Wielkość instancji') 
ylabel('Wartość funkcji celu najlepszego rozwiązania') 
[TT,TT2] = title('Wartość funkcji celu dla różnych typów ruchu dla instancji generowanych losowo o rozmiarze od 10 do 300','FontSize',14)
legend({'invert','swap','insert'},'Location','northwest')
set(P,"Marker",".");
set(P, "MarkerSize", 8);
set(P, "LineWidth", 1.25);
grid on;
%xlim([0,100])