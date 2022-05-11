T1 = readtable('ftv47L30.csv');
T2 = readtable('ftv47L60.csv');
T3 = readtable('ftv47L90.csv');
T4 = readtable('ftv47L120.csv');
T5 = readtable('ftv47L150.csv');
T6 = readtable('ftv47L180.csv');
T7 = readtable('ftv47L210.csv');
T8 = readtable('ftv47L240.csv');
T9 = readtable('ftv47L270.csv');
T10 = readtable('ftv47L300.csv');


K1 = table2array(T1);
K2 = table2array(T2);
K3 = table2array(T3);
K4 = table2array(T4);
K5 = table2array(T5);
K6 = table2array(T6);
K7 = table2array(T7);
K8 = table2array(T8);
K9 = table2array(T9);
K10 = table2array(T10);

x1 = K1(:,1);
y1 = K1(:,2);
x2 = K2(:,1);
y2 = K2(:,2);
x3 = K3(:,1);
y3 = K3(:,2);
x4 = K4(:,1);
y4 = K4(:,2);
x5 = K5(:,1);
y5 = K5(:,2);
x6 = K6(:,1);
y6 = K6(:,2);
x7 = K7(:,1);
y7 = K7(:,2);
x8 = K8(:,1);
y8 = K8(:,2);
x9 = K9(:,1);
y9 = K9(:,2);
x10 = K10(:,1);
y10 = K10(:,2);



P = plot(x1,y1,x2,y2,x3,y3,x4,y4,x5,y5,x6,y6,x7,y7,x8,y8,x9,y9,x10,y10);
xlabel('Długość listy tabu') 
ylabel('Wartość funkcji celu najlepszego rozwiązania') 
[TT,TT2] = title('Wartość funkcji celu dla różnych ilości iteracji oraz różnych długości list tabu dla instancji ftv47','FontSize',16)
legend({'i = 30','i = 60','i = 90','i = 120','i = 150','i = 180','i = 210','i = 240','i = 270','i = 300'},'Location','northeast')
set(P,"Marker",".");
grid on;
xlim([0,100])