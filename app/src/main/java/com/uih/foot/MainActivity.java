```html
<!DOCTYPE html>
<html lang="ar" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>مدير البطولات الدولية</title>
    
    <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://unpkg.com/react@18/umd/react.production.min.js" crossorigin></script>
    <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js" crossorigin></script>
    <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
    
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        cairo: ['Cairo', 'sans-serif'],
                    },
                    colors: {
                        navy: {
                            900: '#0a1128',
                            800: '#121f3d',
                            700: '#1c2e5c',
                        },
                        pitch: {
                            light: '#2ecc71',
                            DEFAULT: '#27ae60',
                            dark: '#229954',
                        },
                        gold: {
                            DEFAULT: '#f1c40f',
                            dark: '#f39c12',
                        }
                    }
                }
            }
        }
    </script>
    <style>
        body {
            font-family: 'Cairo', sans-serif;
            background-color: #0a1128;
            color: #e2e8f0;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }
        .glass-panel {
            background: rgba(18, 31, 61, 0.7);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        .custom-scrollbar::-webkit-scrollbar {
            width: 6px;
            height: 6px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
            background: #0a1128;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
            background: #27ae60;
            border-radius: 4px;
        }
        .bracket-line {
            position: absolute;
            background-color: #4a5568;
            z-index: 0;
        }
        @keyframes pulse-glow {
            0% { box-shadow: 0 0 0 0 rgba(39, 174, 96, 0.7); }
            70% { box-shadow: 0 0 0 10px rgba(39, 174, 96, 0); }
            100% { box-shadow: 0 0 0 0 rgba(39, 174, 96, 0); }
        }
        .active-user-team {
            animation: pulse-glow 2s infinite;
            border-color: #27ae60 !important;
        }
    </style>
</head>
<body>
    <div id="root"></div>

    <script type="text/babel">
        const { useState, useEffect, useMemo, useCallback } = React;

        const DB_TEAMS = [
            // الخليج والعرب
            { id: 'ksa', name: 'السعودية', continent: 'آسيا', rating: 78, flag: '🇸🇦' },
            { id: 'irq', name: 'العراق', continent: 'آسيا', rating: 76, flag: '🇮🇶' },
            { id: 'qat', name: 'قطر', continent: 'آسيا', rating: 79, flag: '🇶🇦' },
            { id: 'uae', name: 'الإمارات', continent: 'آسيا', rating: 75, flag: '🇦🇪' },
            { id: 'oma', name: 'عمان', continent: 'آسيا', rating: 73, flag: '🇴🇲' },
            { id: 'bhr', name: 'البحرين', continent: 'آسيا', rating: 72, flag: '🇧🇭' },
            { id: 'kuw', name: 'الكويت', continent: 'آسيا', rating: 68, flag: '🇰🇼' },
            { id: 'yem', name: 'اليمن', continent: 'آسيا', rating: 60, flag: '🇾🇪' },
            { id: 'egy', name: 'مصر', continent: 'أفريقيا', rating: 79, flag: '🇪🇬' },
            { id: 'mar', name: 'المغرب', continent: 'أفريقيا', rating: 83, flag: '🇲🇦' },
            { id: 'alg', name: 'الجزائر', continent: 'أفريقيا', rating: 80, flag: '🇩🇿' },
            { id: 'tun', name: 'تونس', continent: 'أفريقيا', rating: 77, flag: '🇹🇳' },
            // أوروبا
            { id: 'fra', name: 'فرنسا', continent: 'أوروبا', rating: 92, flag: '🇫🇷' },
            { id: 'eng', name: 'إنجلترا', continent: 'أوروبا', rating: 90, flag: '🏴󠁧󠁢󠁥󠁮󠁧󠁿' },
            { id: 'esp', name: 'إسبانيا', continent: 'أوروبا', rating: 89, flag: '🇪🇸' },
            { id: 'ger', name: 'ألمانيا', continent: 'أوروبا', rating: 88, flag: '🇩🇪' },
            { id: 'ita', name: 'إيطاليا', continent: 'أوروبا', rating: 87, flag: '🇮🇹' },
            { id: 'por', name: 'البرتغال', continent: 'أوروبا', rating: 88, flag: '🇵🇹' },
            { id: 'ned', name: 'هولندا', continent: 'أوروبا', rating: 86, flag: '🇳🇱' },
            { id: 'bel', name: 'بلجيكا', continent: 'أوروبا', rating: 85, flag: '🇧🇪' },
            { id: 'cro', name: 'كرواتيا', continent: 'أوروبا', rating: 84, flag: '🇭🇷' },
            // أمريكا الجنوبية
            { id: 'arg', name: 'الأرجنتين', continent: 'أمريكا الجنوبية', rating: 93, flag: '🇦🇷' },
            { id: 'bra', name: 'البرازيل', continent: 'أمريكا الجنوبية', rating: 91, flag: '🇧🇷' },
            { id: 'uru', name: 'أوروجواي', continent: 'أمريكا الجنوبية', rating: 85, flag: '🇺🇾' },
            { id: 'col', name: 'كولومبيا', continent: 'أمريكا الجنوبية', rating: 82, flag: '🇨🇴' },
            // أخرى
            { id: 'jpn', name: 'اليابان', continent: 'آسيا', rating: 81, flag: '🇯🇵' },
            { id: 'kor', name: 'كوريا الجنوبية', continent: 'آسيا', rating: 80, flag: '🇰🇷' },
            { id: 'aus', name: 'أستراليا', continent: 'آسيا', rating: 77, flag: '🇦🇺' },
            { id: 'sen', name: 'السنغال', continent: 'أفريقيا', rating: 80, flag: '🇸🇳' },
            { id: 'usa', name: 'الولايات المتحدة', continent: 'أمريكا الشمالية', rating: 79, flag: '🇺🇸' },
            { id: 'mex', name: 'المكسيك', continent: 'أمريكا الشمالية', rating: 78, flag: '🇲🇽' },
            { id: 'can', name: 'كندا', continent: 'أمريكا الشمالية', rating: 76, flag: '🇨🇦' },
            { id: 'nzl', name: 'نيوزيلندا', continent: 'أوقيانوسيا', rating: 68, flag: '🇳🇿' },
        ];

        const simulateMatchScore = (team1, team2, mode, isKnockout) => {
            let diff = team1.rating - team2.rating;
            if (mode === 'surprises') diff *= 0.6;
            if (mode === 'realistic') diff *= 1.3;

            // Poisson-like goals generator
            const getGoals = (lambda) => {
                let L = Math.exp(-lambda), p = 1.0, k = 0;
                do { k++; p *= Math.random(); } while (p > L);
                return k - 1;
            };

            let baseGoalProb = 1.2;
            let lambda1 = Math.max(0.3, baseGoalProb + (diff / 25));
            let lambda2 = Math.max(0.3, baseGoalProb - (diff / 25));

            // Knockout matches are generally tighter
            if (isKnockout) {
                lambda1 *= 0.85;
                lambda2 *= 0.85;
            }

            let score1 = getGoals(lambda1);
            let score2 = getGoals(lambda2);

            let isExtraTime = false;
            let pen1 = null, pen2 = null;

            if (isKnockout && score1 === score2) {
                isExtraTime = true;
                // Extra time simulation
                let etLambda1 = lambda1 * 0.35;
                let etLambda2 = lambda2 * 0.35;
                score1 += getGoals(etLambda1);
                score2 += getGoals(etLambda2);

                if (score1 === score2) {
                    // Penalties
                    pen1 = 0; pen2 = 0;
                    let p1Prob = 0.72 + (team1.rating > team2.rating ? 0.08 : 0);
                    let p2Prob = 0.72 + (team2.rating > team1.rating ? 0.08 : 0);
                    
                    for (let i = 0; i < 5; i++) {
                        if (Math.random() < p1Prob) pen1++;
                        if (Math.random() < p2Prob) pen2++;
                    }
                    while (pen1 === pen2) {
                        if (Math.random() < p1Prob) pen1++;
                        if (Math.random() < p2Prob) pen2++;
                    }
                }
            }

            return { score1, score2, isExtraTime, penalty1: pen1, penalty2: pen2 };
        };

        const generateGroupsAndFixtures = (teams, config) => {
            // Shuffle teams for draw
            let shuffled = [...teams].sort(() => Math.random() - 0.5);
            let groups = [];
            let fixtures = [];
            let matchIdCounter = 1;

            const numGroups = config.numGroups || 2;
            const teamsPerGroup = teams.length / numGroups;

            // Generate Groups
            for (let i = 0; i < numGroups; i++) {
                let groupName = String.fromCharCode(1571 + i); // أ, ب, ج...
                if(i===0) groupName = "الأولى";
                if(i===1) groupName = "الثانية";
                if(i===2) groupName = "الثالثة";
                if(i===3) groupName = "الرابعة";
                if(i===4) groupName = "الخامسة";
                if(i===5) groupName = "السادسة";
                if(i===6) groupName = "السابعة";
                if(i===7) groupName = "الثامنة";

                groups.push({
                    id: `G${i+1}`,
                    name: `المجموعة ${groupName}`,
                    teams: shuffled.slice(i * teamsPerGroup, (i + 1) * teamsPerGroup).map(t => t.id)
                });
            }

            // Generate Group Fixtures (Single Round Robin)
            groups.forEach(g => {
                let groupTeams = g.teams;
                for (let i = 0; i < groupTeams.length; i++) {
                    for (let j = i + 1; j < groupTeams.length; j++) {
                        fixtures.push({
                            id: `M${matchIdCounter++}`,
                            stage: 'group',
                            groupId: g.id,
                            team1Id: groupTeams[i],
                            team2Id: groupTeams[j],
                            score1: null,
                            score2: null,
                            isPlayed: false,
                            isSimulated: false
                        });
                    }
                }
            });

            // Determine knockout bracket size based on advancing teams
            let advancingPerGroup = config.advancingPerGroup || 2;
            let totalAdvancing = numGroups * advancingPerGroup;
            
            // Create Knockout placeholders
            let knockoutRounds = [];
            if (totalAdvancing === 16) knockoutRounds = ['R16', 'QF', 'SF', 'F'];
            else if (totalAdvancing === 8) knockoutRounds = ['QF', 'SF', 'F'];
            else if (totalAdvancing === 4) knockoutRounds = ['SF', 'F'];
            else if (totalAdvancing === 2) knockoutRounds = ['F'];

            knockoutRounds.forEach(round => {
                let matchesInRound = 0;
                if(round === 'R16') matchesInRound = 8;
                if(round === 'QF') matchesInRound = 4;
                if(round === 'SF') matchesInRound = 2;
                if(round === 'F') matchesInRound = 1;

                for(let i=0; i<matchesInRound; i++) {
                    fixtures.push({
                        id: `${round}_${i+1}`,
                        stage: 'knockout',
                        round: round,
                        team1Id: null, // Will be filled as tournament progresses
                        team2Id: null,
                        score1: null,
                        score2: null,
                        isPlayed: false,
                        isSimulated: false,
                        isExtraTime: false,
                        penalty1: null,
                        penalty2: null
                    });
                }
            });

            return { groups, fixtures, knockoutRounds };
        };

        const calculateStandings = (groupId, groups, fixtures, teamsDb) => {
            const group = groups.find(g => g.id === groupId);
            if (!group) return [];

            let standings = group.teams.map(tid => {
                const t = teamsDb.find(tdb => tdb.id === tid);
                return { id: tid, name: t.name, flag: t.flag, pld: 0, w: 0, d: 0, l: 0, gf: 0, ga: 0, gd: 0, pts: 0 };
            });

            const groupFixtures = fixtures.filter(f => f.groupId === groupId && f.isPlayed);

            groupFixtures.forEach(f => {
                let t1 = standings.find(s => s.id === f.team1Id);
                let t2 = standings.find(s => s.id === f.team2Id);
                
                t1.pld++; t2.pld++;
                t1.gf += f.score1; t1.ga += f.score2;
                t2.gf += f.score2; t2.ga += f.score1;
                
                if (f.score1 > f.score2) { t1.w++; t1.pts += 3; t2.l++; }
                else if (f.score1 < f.score2) { t2.w++; t2.pts += 3; t1.l++; }
                else { t1.d++; t2.d++; t1.pts += 1; t2.pts += 1; }
            });

            standings.forEach(s => s.gd = s.gf - s.ga);

            // Sort: Pts > GD > GF
            standings.sort((a, b) => {
                if (b.pts !== a.pts) return b.pts - a.pts;
                if (b.gd !== a.gd) return b.gd - a.gd;
                return b.gf - a.gf;
            });

            return standings;
        };

        const App = () => {
            const [activeView, setActiveView] = useState('home');
            const [tournament, setTournament] = useState(null);
            
            // Load state on mount
            useEffect(() => {
                const saved = localStorage.getItem('tournamentManagerData');
                if (saved) {
                    setTournament(JSON.parse(saved));
                }
            }, []);

            // Save state on change
            useEffect(() => {
                if (tournament) {
                    localStorage.setItem('tournamentManagerData', JSON.stringify(tournament));
                }
            }, [tournament]);

            const deleteTournament = () => {
                if (confirm("هل أنت متأكد من حذف البطولة؟ لا يمكن التراجع عن هذا الإجراء.")) {
                    localStorage.removeItem('tournamentManagerData');
                    setTournament(null);
                    setActiveView('home');
                }
            };

            const renderView = () => {
                if (!tournament && activeView !== 'wizard') {
                    return <WelcomeView onStartWizard={() => setActiveView('wizard')} />;
                }
                
                switch (activeView) {
                    case 'home': return <Dashboard tournament={tournament} setTournament={setTournament} setActiveView={setActiveView} />;
                    case 'wizard': return <TournamentWizard setTournament={setTournament} setActiveView={setActiveView} />;
                    case 'groups': return <GroupsView tournament={tournament} />;
                    case 'fixtures': return <FixturesView tournament={tournament} setTournament={setTournament} />;
                    case 'knockout': return <KnockoutView tournament={tournament} setTournament={setTournament} />;
                    case 'settings': return <SettingsView tournament={tournament} deleteTournament={deleteTournament} />;
                    default: return <Dashboard tournament={tournament} setTournament={setTournament} setActiveView={setActiveView} />;
                }
            };

            return (
                <div className="min-h-screen flex flex-col md:flex-row pb-16 md:pb-0">
                    {/* Navigation Sidebar/Bottom Bar */}
                    <nav className="bg-navy-800 border-b md:border-b-0 md:border-l border-gray-700 w-full md:w-64 fixed bottom-0 md:relative z-50 flex md:flex-col justify-around md:justify-start p-2 md:p-4 gap-2 shadow-2xl">
                        <div className="hidden md:flex items-center gap-3 mb-8 px-2 text-white">
                            <i className="fas fa-trophy text-gold text-2xl"></i>
                            <span className="font-bold text-lg tracking-wide">مدير البطولات</span>
                        </div>
                        
                        <NavBtn icon="fa-home" label="الرئيسية" active={activeView==='home'} onClick={() => setActiveView('home')} />
                        {tournament && <NavBtn icon="fa-users" label="المجموعات" active={activeView==='groups'} onClick={() => setActiveView('groups')} />}
                        {tournament && <NavBtn icon="fa-calendar-alt" label="المباريات" active={activeView==='fixtures'} onClick={() => setActiveView('fixtures')} />}
                        {tournament && <NavBtn icon="fa-sitemap" label="الإقصائيات" active={activeView==='knockout'} onClick={() => setActiveView('knockout')} />}
                        {tournament && <NavBtn icon="fa-cog" label="الإعدادات" active={activeView==='settings'} onClick={() => setActiveView('settings')} />}
                    </nav>

                    {/* Main Content Area */}
                    <main className="flex-1 overflow-y-auto h-screen custom-scrollbar relative p-4 md:p-8">
                        {renderView()}
                    </main>
                </div>
            );
        };

        const NavBtn = ({ icon, label, active, onClick }) => (
            <button 
                onClick={onClick}
                className={`flex md:flex-row flex-col items-center md:justify-start justify-center gap-2 p-3 rounded-xl transition-all duration-300 ${active ? 'bg-pitch text-white shadow-lg shadow-pitch-dark/40' : 'text-gray-400 hover:bg-navy-700 hover:text-white'}`}
            >
                <i className={`fas ${icon} text-xl md:text-lg mb-1 md:mb-0`}></i>
                <span className="text-xs md:text-sm font-semibold">{label}</span>
            </button>
        );

        const WelcomeView = ({ onStartWizard }) => (
            <div className="flex flex-col items-center justify-center h-full text-center max-w-2xl mx-auto space-y-8 animate-fade-in mt-20">
                <div className="w-32 h-32 bg-navy-800 rounded-full flex items-center justify-center border-4 border-gold shadow-2xl shadow-gold/20 mb-4">
                    <i className="fas fa-globe text-6xl text-gold"></i>
                </div>
                <h1 className="text-4xl md:text-5xl font-bold text-white mb-2 leading-tight">مدير البطولات الدولية</h1>
                <p className="text-gray-400 text-lg">صمم بطولتك الخاصة، العب بمنتخبك المفضل، ودع الذكاء الاصطناعي يحاكي بقية المباريات بواقعية.</p>
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 w-full mt-8">
                    <button onClick={onStartWizard} className="bg-pitch hover:bg-pitch-dark text-white font-bold py-4 px-6 rounded-xl transition shadow-lg shadow-pitch/30 flex items-center justify-center gap-3">
                        <i className="fas fa-plus-circle text-xl"></i>
                        <span>إنشاء بطولة جديدة</span>
                    </button>
                    <button className="bg-navy-700 hover:bg-navy-600 text-white font-bold py-4 px-6 rounded-xl transition flex items-center justify-center gap-3 opacity-50 cursor-not-allowed" title="قريباً">
                        <i className="fas fa-folder-open text-xl"></i>
                        <span>تحميل بطولة محفوظة</span>
                    </button>
                </div>

                <div className="mt-12 glass-panel p-6 rounded-2xl text-right w-full">
                    <h3 className="text-xl font-bold text-gold mb-3 flex items-center gap-2"><i className="fas fa-lightbulb"></i> مميزات التطبيق</h3>
                    <ul className="text-gray-300 space-y-2 list-disc list-inside">
                        <li>دعم من 4 إلى 64 منتخباً وطنياً من جميع القارات.</li>
                        <li>محاكاة دقيقة وواقعية لنتائج المباريات بناءً على قوة المنتخبات.</li>
                        <li>جداول ترتيب تلقائية وتحديث لحظي.</li>
                        <li>إنشاء أدوار إقصائية تلقائياً وتتبع مسار البطل.</li>
                        <li>الوضع المثالي للعب كلعبة مصاحبة (Companion App).</li>
                    </ul>
                </div>
            </div>
        );

        const TournamentWizard = ({ setTournament, setActiveView }) => {
            const [step, setStep] = useState(1);
            
            // Wizard State
            const [info, setInfo] = useState({ name: 'كأس العالم الافتراضي', edition: '1', year: '2026', host: 'تحديد تلقائي' });
            const [selectedTeamIds, setSelectedTeamIds] = useState([]);
            const [userTeamId, setUserTeamId] = useState('');
            const [format, setFormat] = useState({ type: 'groups', numGroups: 8, advancingPerGroup: 2, simMode: 'balanced' });

            const fillGulfCupTemplate = () => {
                setInfo({ name: 'كأس الخليج العربي', edition: '27', year: '2026', host: 'السعودية' });
                setSelectedTeamIds(['ksa', 'irq', 'qat', 'uae', 'oma', 'bhr', 'kuw', 'yem']);
                setFormat({ type: 'groups', numGroups: 2, advancingPerGroup: 2, simMode: 'balanced' });
                setStep(2);
            };

            const toggleTeam = (id) => {
                setSelectedTeamIds(prev => prev.includes(id) ? prev.filter(t => t !== id) : [...prev, id]);
            };

            const handleFinish = () => {
                if(!userTeamId) { alert('الرجاء اختيار منتخب للتحكم به.'); return; }
                
                const selectedTeamsData = selectedTeamIds.map(id => DB_TEAMS.find(t => t.id === id));
                const { groups, fixtures, knockoutRounds } = generateGroupsAndFixtures(selectedTeamsData, format);

                const newTournament = {
                    info,
                    teams: selectedTeamsData,
                    userTeamId,
                    format,
                    groups,
                    fixtures,
                    knockoutRounds,
                    stage: 'group', // group, knockout, finished
                    currentMatchday: 1
                };
                
                setTournament(newTournament);
                setActiveView('home');
            };

            return (
                <div className="max-w-4xl mx-auto py-6">
                    {/* Stepper Header */}
                    <div className="flex justify-between items-center mb-8 relative">
                        <div className="absolute top-1/2 left-0 right-0 h-1 bg-navy-700 -z-10 -translate-y-1/2 rounded"></div>
                        {[1, 2, 3, 4].map(num => (
                            <div key={num} className={`w-10 h-10 rounded-full flex items-center justify-center font-bold border-4 transition-colors ${step >= num ? 'bg-pitch border-navy-800 text-white shadow-[0_0_15px_rgba(39,174,96,0.5)]' : 'bg-navy-800 border-navy-600 text-gray-500'}`}>
                                {num}
                            </div>
                        ))}
                    </div>

                    <div className="glass-panel p-6 md:p-8 rounded-2xl shadow-xl">
                        {step === 1 && (
                            <div className="animate-fade-in">
                                <div className="flex justify-between items-center mb-6">
                                    <h2 className="text-2xl font-bold text-white border-b-2 border-pitch inline-block pb-2">المعلومات الأساسية</h2>
                                    <button onClick={fillGulfCupTemplate} className="text-xs md:text-sm bg-navy-700 hover:bg-navy-600 text-gold py-2 px-4 rounded-lg transition border border-gold/30">
                                        <i className="fas fa-magic ml-2"></i> قالب كأس الخليج
                                    </button>
                                </div>
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                    <div>
                                        <label className="block text-gray-400 mb-2">اسم البطولة</label>
                                        <input type="text" className="w-full bg-navy-900 border border-gray-600 rounded-xl p-3 text-white focus:border-pitch focus:outline-none transition" value={info.name} onChange={e=>setInfo({...info, name: e.target.value})} />
                                    </div>
                                    <div>
                                        <label className="block text-gray-400 mb-2">رقم النسخة</label>
                                        <input type="text" className="w-full bg-navy-900 border border-gray-600 rounded-xl p-3 text-white focus:border-pitch focus:outline-none transition" value={info.edition} onChange={e=>setInfo({...info, edition: e.target.value})} />
                                    </div>
                                    <div>
                                        <label className="block text-gray-400 mb-2">السنة</label>
                                        <input type="text" className="w-full bg-navy-900 border border-gray-600 rounded-xl p-3 text-white focus:border-pitch focus:outline-none transition" value={info.year} onChange={e=>setInfo({...info, year: e.target.value})} />
                                    </div>
                                    <div>
                                        <label className="block text-gray-400 mb-2">الدولة المستضيفة</label>
                                        <input type="text" className="w-full bg-navy-900 border border-gray-600 rounded-xl p-3 text-white focus:border-pitch focus:outline-none transition" value={info.host} onChange={e=>setInfo({...info, host: e.target.value})} />
                                    </div>
                                </div>
                            </div>
                        )}

                        {step === 2 && (
                            <div className="animate-fade-in">
                                <h2 className="text-2xl font-bold text-white border-b-2 border-pitch inline-block pb-2 mb-2">اختيار المنتخبات المشاركة</h2>
                                <p className="text-gray-400 mb-6 text-sm">اختر من 4 إلى 64 منتخباً. (العدد الحالي: <span className={selectedTeamIds.length >= 4 ? 'text-pitch font-bold' : 'text-red-500'}>{selectedTeamIds.length}</span>)</p>
                                
                                <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-3 max-h-96 overflow-y-auto custom-scrollbar p-2">
                                    {DB_TEAMS.map(team => (
                                        <div key={team.id} onClick={() => toggleTeam(team.id)} className={`cursor-pointer p-3 rounded-xl border flex flex-col items-center justify-center transition-all ${selectedTeamIds.includes(team.id) ? 'bg-pitch/20 border-pitch text-white' : 'bg-navy-900 border-gray-700 text-gray-400 hover:border-gray-500'}`}>
                                            <span className="text-3xl mb-2">{team.flag}</span>
                                            <span className="font-semibold text-sm text-center">{team.name}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {step === 3 && (
                            <div className="animate-fade-in">
                                <h2 className="text-2xl font-bold text-white border-b-2 border-pitch inline-block pb-2 mb-4">اختيار منتخبك</h2>
                                <div className="bg-navy-900 p-4 rounded-xl mb-6 border-l-4 border-gold text-gray-300">
                                    <i className="fas fa-info-circle text-gold mr-2 ml-2"></i>
                                    اختر المنتخب الذي ستلعب مبارياته داخل اللعبة. ستقوم بإدخال نتائج هذا المنتخب يدوياً، بينما سيحاكي النظام جميع المباريات الأخرى.
                                </div>
                                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 max-h-80 overflow-y-auto custom-scrollbar p-2">
                                    {selectedTeamIds.map(id => {
                                        const team = DB_TEAMS.find(t => t.id === id);
                                        return (
                                            <div key={team.id} onClick={() => setUserTeamId(team.id)} className={`cursor-pointer p-4 rounded-xl border-2 flex items-center gap-3 transition-all ${userTeamId === team.id ? 'bg-navy-800 border-gold shadow-lg shadow-gold/20 scale-105' : 'bg-navy-900 border-gray-700 hover:border-gray-500'}`}>
                                                <span className="text-2xl">{team.flag}</span>
                                                <span className="font-bold text-white">{team.name}</span>
                                            </div>
                                        );
                                    })}
                                </div>
                            </div>
                        )}

                        {step === 4 && (
                            <div className="animate-fade-in">
                                <h2 className="text-2xl font-bold text-white border-b-2 border-pitch inline-block pb-2 mb-6">نظام البطولة والمحاكاة</h2>
                                
                                <div className="space-y-6">
                                    <div className="bg-navy-900 p-5 rounded-xl border border-gray-700">
                                        <h3 className="text-lg font-bold text-gold mb-4">هيكلية المجموعات</h3>
                                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-gray-400 mb-2">عدد المجموعات</label>
                                                <select className="w-full bg-navy-800 border border-gray-600 rounded-lg p-3 text-white focus:outline-none" value={format.numGroups} onChange={e=>setFormat({...format, numGroups: parseInt(e.target.value)})}>
                                                    <option value={2}>مجموعتان (2)</option>
                                                    <option value={4}>4 مجموعات</option>
                                                    <option value={8}>8 مجموعات</option>
                                                </select>
                                                <span className="text-xs text-gray-500 mt-1 block">تأكد من توافق العدد مع إجمالي المنتخبات ({selectedTeamIds.length})</span>
                                            </div>
                                            <div>
                                                <label className="block text-gray-400 mb-2">المتأهلون من كل مجموعة</label>
                                                <select className="w-full bg-navy-800 border border-gray-600 rounded-lg p-3 text-white focus:outline-none" value={format.advancingPerGroup} onChange={e=>setFormat({...format, advancingPerGroup: parseInt(e.target.value)})}>
                                                    <option value={2}>الأول والثاني (2)</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="bg-navy-900 p-5 rounded-xl border border-gray-700">
                                        <h3 className="text-lg font-bold text-pitch mb-4">محرك المحاكاة</h3>
                                        <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                                            <button onClick={() => setFormat({...format, simMode: 'realistic'})} className={`p-4 rounded-lg border text-center transition ${format.simMode === 'realistic' ? 'bg-pitch/20 border-pitch text-white' : 'bg-navy-800 border-gray-600 text-gray-400'}`}>
                                                <i className="fas fa-balance-scale-right block text-2xl mb-2"></i>
                                                واقعية عالية<br/><span className="text-xs opacity-70">يفضل المنتخبات القوية بشدة</span>
                                            </button>
                                            <button onClick={() => setFormat({...format, simMode: 'balanced'})} className={`p-4 rounded-lg border text-center transition ${format.simMode === 'balanced' ? 'bg-pitch/20 border-pitch text-white' : 'bg-navy-800 border-gray-600 text-gray-400'}`}>
                                                <i className="fas fa-balance-scale block text-2xl mb-2"></i>
                                                متوازنة<br/><span className="text-xs opacity-70">واقعية مع بعض المفاجآت</span>
                                            </button>
                                            <button onClick={() => setFormat({...format, simMode: 'surprises'})} className={`p-4 rounded-lg border text-center transition ${format.simMode === 'surprises' ? 'bg-pitch/20 border-pitch text-white' : 'bg-navy-800 border-gray-600 text-gray-400'}`}>
                                                <i className="fas fa-dice block text-2xl mb-2"></i>
                                                مفاجآت أكثر<br/><span className="text-xs opacity-70">نتائج غير متوقعة بكثرة</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Navigation Buttons */}
                        <div className="flex justify-between mt-10 pt-6 border-t border-gray-700">
                            {step > 1 ? (
                                <button onClick={() => setStep(step - 1)} className="px-6 py-3 rounded-xl bg-navy-700 hover:bg-navy-600 text-white font-bold transition">السابق</button>
                            ) : <div></div>}
                            
                            {step < 4 ? (
                                <button onClick={() => {
                                    if(step === 2 && (selectedTeamIds.length < 4 || selectedTeamIds.length % 2 !== 0)) {
                                        alert("الرجاء اختيار عدد منتخبات صحيح (زوجي، بحد أدنى 4)."); return;
                                    }
                                    setStep(step + 1);
                                }} className="px-8 py-3 rounded-xl bg-pitch hover:bg-pitch-dark text-white font-bold transition shadow-lg">التالي</button>
                            ) : (
                                <button onClick={handleFinish} className="px-8 py-3 rounded-xl bg-gold hover:bg-gold-dark text-navy-900 font-bold transition shadow-lg shadow-gold/30 flex items-center gap-2">
                                    <i className="fas fa-check"></i> تأكيد وإنشاء البطولة
                                </button>
                            )}
                        </div>
                    </div>
                </div>
            );
        };

        const Dashboard = ({ tournament, setTournament, setActiveView }) => {
            if (!tournament) return null;

            const userTeam = tournament.teams.find(t => t.id === tournament.userTeamId);
            
            // Find next user match
            const nextUserMatch = tournament.fixtures.find(f => !f.isPlayed && (f.team1Id === userTeam.id || f.team2Id === userTeam.id));
            
            // Calc stats
            const totalMatches = tournament.fixtures.filter(f => f.team1Id && f.team2Id).length;
            const playedMatches = tournament.fixtures.filter(f => f.isPlayed).length;
            const progress = totalMatches > 0 ? (playedMatches / totalMatches) * 100 : 0;

            return (
                <div className="animate-fade-in space-y-6 max-w-6xl mx-auto">
                    {/* Header Card */}
                    <div className="glass-panel rounded-2xl p-6 md:p-8 flex flex-col md:flex-row justify-between items-center gap-6 relative overflow-hidden">
                        <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-pitch via-gold to-pitch"></div>
                        <div className="text-center md:text-right z-10">
                            <h1 className="text-3xl md:text-4xl font-black text-white mb-2">{tournament.info.name}</h1>
                            <p className="text-gray-400 text-lg"><i className="fas fa-calendar-alt text-gold mr-1 ml-1"></i> النسخة {tournament.info.edition} - {tournament.info.year} | <i className="fas fa-map-marker-alt text-pitch mr-1 ml-1"></i> {tournament.info.host}</p>
                        </div>
                        <div className="flex flex-col items-center bg-navy-900/80 p-4 rounded-2xl border border-gold/30 z-10 min-w-[200px] active-user-team">
                            <span className="text-xs text-gold font-bold mb-1">منتخبك الحالي</span>
                            <span className="text-5xl mb-2 drop-shadow-lg">{userTeam.flag}</span>
                            <span className="text-xl font-bold text-white">{userTeam.name}</span>
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* Next Match Card */}
                        <div className="md:col-span-2 glass-panel rounded-2xl p-6 border-t-4 border-pitch">
                            <h2 className="text-xl font-bold mb-6 flex items-center gap-2 text-white">
                                <i className="fas fa-forward text-pitch"></i> مباراتك القادمة
                            </h2>
                            {nextUserMatch ? (
                                <div className="flex flex-col items-center">
                                    <span className="text-sm text-gray-400 bg-navy-800 px-4 py-1 rounded-full mb-6 border border-gray-700">
                                        {nextUserMatch.stage === 'group' ? 'دور المجموعات' : 'الأدوار الإقصائية'}
                                    </span>
                                    <div className="flex items-center justify-center gap-4 md:gap-12 w-full">
                                        <TeamDisplay teamId={nextUserMatch.team1Id} teams={tournament.teams} isUser={nextUserMatch.team1Id === userTeam.id} />
                                        <div className="text-2xl font-black text-gray-500 bg-navy-900 w-12 h-12 rounded-full flex items-center justify-center border border-gray-700">VS</div>
                                        <TeamDisplay teamId={nextUserMatch.team2Id} teams={tournament.teams} isUser={nextUserMatch.team2Id === userTeam.id} />
                                    </div>
                                    <button onClick={() => setActiveView('fixtures')} className="mt-8 bg-pitch hover:bg-pitch-dark text-white font-bold py-3 px-8 rounded-xl transition w-full md:w-auto shadow-lg shadow-pitch/30">
                                        الذهاب للمباراة وإدخال النتيجة
                                    </button>
                                </div>
                            ) : (
                                <div className="text-center py-12 text-gray-400">
                                    <i className="fas fa-check-circle text-4xl text-pitch mb-4 block"></i>
                                    لا توجد مباريات قادمة لمنتخبك حالياً.
                                </div>
                            )}
                        </div>

                        {/* Tournament Progress */}
                        <div className="glass-panel rounded-2xl p-6">
                            <h2 className="text-xl font-bold mb-4 text-white">تقدم البطولة</h2>
                            <div className="mb-4">
                                <div className="flex justify-between text-sm mb-2">
                                    <span className="text-gray-400">ملعوبة: {playedMatches}</span>
                                    <span className="text-gray-400">المتبقي: {totalMatches - playedMatches}</span>
                                </div>
                                <div className="w-full bg-navy-900 rounded-full h-3 border border-gray-700">
                                    <div className="bg-gradient-to-r from-pitch-dark to-pitch h-3 rounded-full transition-all duration-1000" style={{width: `${progress}%`}}></div>
                                </div>
                            </div>
                            
                            <div className="mt-8 space-y-3">
                                <button onClick={() => setActiveView('groups')} className="w-full bg-navy-800 hover:bg-navy-700 text-white p-3 rounded-xl transition flex justify-between items-center border border-gray-700">
                                    <span>جداول المجموعات</span>
                                    <i className="fas fa-chevron-left text-gray-500"></i>
                                </button>
                                <button onClick={() => setActiveView('knockout')} className="w-full bg-navy-800 hover:bg-navy-700 text-white p-3 rounded-xl transition flex justify-between items-center border border-gray-700">
                                    <span>الأدوار الإقصائية</span>
                                    <i className="fas fa-chevron-left text-gray-500"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            );
        };

        const TeamDisplay = ({ teamId, teams, isUser, size = 'lg' }) => {
            if (!teamId) return <div className="text-gray-600 text-center w-24">مجهول</div>;
            const team = teams.find(t => t.id === teamId);
            return (
                <div className={`flex flex-col items-center ${isUser ? 'active-user-team p-2 rounded-xl' : ''}`}>
                    <span className={`${size === 'lg' ? 'text-6xl mb-2' : 'text-3xl mb-1'} drop-shadow-md`}>{team.flag}</span>
                    <span className={`font-bold ${size === 'lg' ? 'text-xl' : 'text-sm'} text-white text-center whitespace-nowrap`}>{team.name}</span>
                </div>
            );
        }

        const GroupsView = ({ tournament }) => {
            if (!tournament) return null;

            return (
                <div className="animate-fade-in max-w-6xl mx-auto pb-10">
                    <h2 className="text-3xl font-black text-white mb-8 border-r-4 border-pitch pr-4">ترتيب المجموعات</h2>
                    
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                        {tournament.groups.map(group => {
                            const standings = calculateStandings(group.id, tournament.groups, tournament.fixtures, tournament.teams);
                            return (
                                <div key={group.id} className="glass-panel rounded-2xl overflow-hidden shadow-xl">
                                    <div className="bg-navy-900 py-3 px-4 border-b border-gray-700 flex justify-between items-center">
                                        <h3 className="font-bold text-lg text-gold">{group.name}</h3>
                                    </div>
                                    <div className="overflow-x-auto">
                                        <table className="w-full text-sm text-right">
                                            <thead className="bg-navy-800/50 text-gray-400 text-xs uppercase">
                                                <tr>
                                                    <th className="px-4 py-3">المركز</th>
                                                    <th className="px-4 py-3">المنتخب</th>
                                                    <th className="px-2 py-3 text-center" title="لعب">ل</th>
                                                    <th className="px-2 py-3 text-center" title="فاز">ف</th>
                                                    <th className="px-2 py-3 text-center" title="تعادل">ت</th>
                                                    <th className="px-2 py-3 text-center" title="خسر">خ</th>
                                                    <th className="px-2 py-3 text-center hidden sm:table-cell" title="له">له</th>
                                                    <th className="px-2 py-3 text-center hidden sm:table-cell" title="عليه">ع</th>
                                                    <th className="px-2 py-3 text-center" title="فارق الأهداف">ف.أ</th>
                                                    <th className="px-4 py-3 text-center font-bold text-white">نقاط</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {standings.map((team, index) => (
                                                    <tr key={team.id} className={`border-b border-gray-700/50 transition hover:bg-navy-700/50 ${team.id === tournament.userTeamId ? 'bg-pitch/10 font-bold' : ''}`}>
                                                        <td className="px-4 py-3">
                                                            <span className={`w-6 h-6 rounded-full flex items-center justify-center text-xs ${index < tournament.format.advancingPerGroup ? 'bg-pitch text-white' : 'bg-navy-700 text-gray-400'}`}>
                                                                {index + 1}
                                                            </span>
                                                        </td>
                                                        <td className="px-4 py-3 flex items-center gap-2">
                                                            <span className="text-xl">{team.flag}</span>
                                                            <span className="whitespace-nowrap">{team.name}</span>
                                                        </td>
                                                        <td className="px-2 py-3 text-center">{team.pld}</td>
                                                        <td className="px-2 py-3 text-center text-pitch-light">{team.w}</td>
                                                        <td className="px-2 py-3 text-center text-gray-400">{team.d}</td>
                                                        <td className="px-2 py-3 text-center text-red-400">{team.l}</td>
                                                        <td className="px-2 py-3 text-center hidden sm:table-cell">{team.gf}</td>
                                                        <td className="px-2 py-3 text-center hidden sm:table-cell">{team.ga}</td>
                                                        <td className="px-2 py-3 text-center" dir="ltr">{team.gd > 0 ? `+${team.gd}` : team.gd}</td>
                                                        <td className="px-4 py-3 text-center font-bold text-gold text-base">{team.pts}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            );
        };

        const FixturesView = ({ tournament, setTournament }) => {
            const [filter, setFilter] = useState('all'); // all, pending, completed
            const [selectedMatch, setSelectedMatch] = useState(null);

            if (!tournament) return null;

            const handleSimulateNextRound = () => {
                let updatedFixtures = [...tournament.fixtures];
                let changed = false;

                const unplayed = updatedFixtures.filter(f => !f.isPlayed && f.team1Id && f.team2Id);
                if (unplayed.length === 0) {
                    alert("لا توجد مباريات متاحة للمحاكاة حالياً.");
                    return;
                }

                const currentStage = unplayed[0].stage;
                const currentRound = unplayed[0].round;

                let teamsSimulatedThisBatch = new Set();
                let matchesToSimulate = [];

                unplayed.forEach(f => {
                    if (f.stage === currentStage) {
                        if (currentStage === 'knockout' && f.round !== currentRound) return;
                        
                        // محاكاة مباراة واحدة فقط لكل فريق في هذه الدفعة (محاكاة جولة بجولة)
                        if (!teamsSimulatedThisBatch.has(f.team1Id) && !teamsSimulatedThisBatch.has(f.team2Id)) {
                            matchesToSimulate.push(f);
                            teamsSimulatedThisBatch.add(f.team1Id);
                            teamsSimulatedThisBatch.add(f.team2Id);
                        }
                    }
                });

                let userMatchPending = false;

                matchesToSimulate.forEach(f => {
                    if (f.team1Id === tournament.userTeamId || f.team2Id === tournament.userTeamId) {
                        userMatchPending = true;
                    } else {
                        const t1 = tournament.teams.find(t => t.id === f.team1Id);
                        const t2 = tournament.teams.find(t => t.id === f.team2Id);
                        const result = simulateMatchScore(t1, t2, tournament.format.simMode, f.stage === 'knockout');
                        
                        let targetFixture = updatedFixtures.find(uf => uf.id === f.id);
                        targetFixture.score1 = result.score1;
                        targetFixture.score2 = result.score2;
                        targetFixture.isExtraTime = result.isExtraTime;
                        targetFixture.penalty1 = result.penalty1;
                        targetFixture.penalty2 = result.penalty2;
                        targetFixture.isPlayed = true;
                        targetFixture.isSimulated = true;
                        changed = true;
                    }
                });

                if (changed) {
                    processKnockoutAdvancements(updatedFixtures, tournament);
                    setTournament({ ...tournament, fixtures: updatedFixtures });
                } else if (userMatchPending) {
                    alert("الجولة الحالية تتضمن مباراة لمنتخبك. يرجى إدخال نتيجتك أولاً.");
                }
            };

            const processKnockoutAdvancements = (fixtures, t) => {
                // If group stage is fully completed, generate Knockout initial matchups
                const groupMatches = fixtures.filter(f => f.stage === 'group');
                const allGroupPlayed = groupMatches.length > 0 && groupMatches.every(f => f.isPlayed);
                
                const knockoutMatches = fixtures.filter(f => f.stage === 'knockout');
                const hasFilledKnockouts = knockoutMatches.some(f => f.team1Id !== null);

                if (allGroupPlayed && !hasFilledKnockouts && knockoutMatches.length > 0) {
                    // Logic for advancing teams based on standings
                    let advancingTeams = [];
                    t.groups.forEach(g => {
                        const std = calculateStandings(g.id, t.groups, fixtures, t.teams);
                        for(let i=0; i<t.format.advancingPerGroup; i++) {
                            advancingTeams.push({ ...std[i], sourceGroup: g.id, rank: i+1 });
                        }
                    });

                    // Very simplified pairing logic for demo: 1A vs 2B, 1B vs 2A etc.
                    // This pairs top team from one group with second team of another.
                    let qfIndex = 0;
                    const sfMatches = fixtures.filter(f => f.round === 'SF');
                    
                    if (t.groups.length === 2 && t.format.advancingPerGroup === 2) {
                        // Gulf Cup style (4 teams advancing total -> 2 SF matches)
                        const g1 = advancingTeams.filter(a => a.sourceGroup === 'G1');
                        const g2 = advancingTeams.filter(a => a.sourceGroup === 'G2');
                        if (sfMatches.length >= 2) {
                            sfMatches[0].team1Id = g1[0].id; // 1A
                            sfMatches[0].team2Id = g2[1].id; // 2B
                            sfMatches[1].team1Id = g2[0].id; // 1B
                            sfMatches[1].team2Id = g1[1].id; // 2A
                        }
                    }
                    // For World cup 8 groups style (R16): omitted for brevity, would require full cross-pairing logic
                }

                // Advance winners in knockout bracket
                const rounds = ['R16', 'QF', 'SF', 'F'];
                for (let i = 0; i < rounds.length - 1; i++) {
                    const currentRound = rounds[i];
                    const nextRound = rounds[i+1];
                    const matchesCurrent = fixtures.filter(f => f.round === currentRound);
                    const matchesNext = fixtures.filter(f => f.round === nextRound);
                    
                    if (matchesCurrent.length > 0 && matchesCurrent.every(f => f.isPlayed)) {
                        // populate next round
                        for (let j = 0; j < matchesNext.length; j++) {
                            if (!matchesNext[j].team1Id && !matchesNext[j].isPlayed) {
                                // Match j gets winners of current round j*2 and j*2 + 1
                                const m1 = matchesCurrent[j*2];
                                const m2 = matchesCurrent[j*2 + 1];
                                if (m1 && m2) {
                                    let w1 = getWinner(m1);
                                    let w2 = getWinner(m2);
                                    matchesNext[j].team1Id = w1;
                                    matchesNext[j].team2Id = w2;
                                }
                            }
                        }
                    }
                }
            };

            const getWinner = (match) => {
                if (match.score1 > match.score2) return match.team1Id;
                if (match.score2 > match.score1) return match.team2Id;
                if (match.penalty1 > match.penalty2) return match.team1Id;
                return match.team2Id;
            };

            const filteredFixtures = tournament.fixtures.filter(f => {
                if (!f.team1Id || !f.team2Id) return false; // hide placeholders
                if (filter === 'pending') return !f.isPlayed;
                if (filter === 'completed') return f.isPlayed;
                return true;
            });

            return (
                <div className="animate-fade-in max-w-5xl mx-auto pb-20">
                    <div className="flex flex-col md:flex-row justify-between items-center mb-8 gap-4">
                        <h2 className="text-3xl font-black text-white border-r-4 border-pitch pr-4">جدول المباريات</h2>
                        
                        <div className="flex gap-2 bg-navy-800 p-1 rounded-xl border border-gray-700">
                            <button onClick={()=>setFilter('all')} className={`px-4 py-2 rounded-lg text-sm transition ${filter==='all'?'bg-pitch text-white':'text-gray-400 hover:text-white'}`}>الكل</button>
                            <button onClick={()=>setFilter('pending')} className={`px-4 py-2 rounded-lg text-sm transition ${filter==='pending'?'bg-pitch text-white':'text-gray-400 hover:text-white'}`}>القادمة</button>
                            <button onClick={()=>setFilter('completed')} className={`px-4 py-2 rounded-lg text-sm transition ${filter==='completed'?'bg-pitch text-white':'text-gray-400 hover:text-white'}`}>المنتهية</button>
                        </div>
                        
                        <button onClick={handleSimulateNextRound} className="bg-navy-700 hover:bg-navy-600 text-gold border border-gold/30 px-4 py-2 rounded-xl text-sm font-bold transition flex items-center gap-2 shadow-lg">
                            <i className="fas fa-robot"></i> محاكاة الجولة الحالية
                        </button>
                    </div>

                    <div className="space-y-4">
                        {filteredFixtures.length === 0 ? (
                            <div className="text-center text-gray-500 py-10 glass-panel rounded-xl">لا توجد مباريات لعرضها في هذا القسم حالياً.</div>
                        ) : (
                            filteredFixtures.map(match => (
                                <MatchCard 
                                    key={match.id} 
                                    match={match} 
                                    teams={tournament.teams} 
                                    userTeamId={tournament.userTeamId}
                                    onAction={() => setSelectedMatch(match)}
                                />
                            ))
                        )}
                    </div>

                    {selectedMatch && (
                        <MatchEntryModal 
                            match={selectedMatch} 
                            teams={tournament.teams}
                            isUserMatch={selectedMatch.team1Id === tournament.userTeamId || selectedMatch.team2Id === tournament.userTeamId}
                            onClose={() => setSelectedMatch(null)}
                            onSave={(result) => {
                                let updated = [...tournament.fixtures];
                                let idx = updated.findIndex(f => f.id === selectedMatch.id);
                                updated[idx] = { ...updated[idx], ...result, isPlayed: true, isSimulated: false };
                                processKnockoutAdvancements(updated, tournament);
                                setTournament({ ...tournament, fixtures: updated });
                                setSelectedMatch(null);
                            }}
                        />
                    )}
                </div>
            );
        };

        const MatchCard = ({ match, teams, userTeamId, onAction }) => {
            const t1 = teams.find(t => t.id === match.team1Id);
            const t2 = teams.find(t => t.id === match.team2Id);
            const isUserMatch = t1.id === userTeamId || t2.id === userTeamId;

            return (
                <div className={`glass-panel p-4 md:p-5 rounded-xl border-l-4 transition hover:bg-navy-800 flex flex-col md:flex-row items-center justify-between gap-4 ${isUserMatch ? 'border-pitch shadow-lg shadow-pitch/10' : 'border-gray-600'}`}>
                    
                    <div className="text-xs text-gray-400 font-bold bg-navy-900 px-3 py-1 rounded w-full md:w-32 text-center border border-gray-700">
                        {match.stage === 'group' ? 'مجموعات' : match.round}
                        {match.isSimulated && <span className="block text-[10px] text-gold mt-1">تمت المحاكاة</span>}
                    </div>

                    <div className="flex items-center justify-center gap-4 flex-1 w-full">
                        <div className={`flex items-center gap-2 flex-1 justify-end ${t1.id === userTeamId ? 'text-pitch font-bold' : 'text-white'}`}>
                            <span className="hidden sm:inline">{t1.name}</span>
                            <span className="text-2xl">{t1.flag}</span>
                        </div>

                        <div className="bg-navy-900 px-4 py-2 rounded-lg font-black text-xl min-w-[80px] text-center border border-gray-700">
                            {match.isPlayed ? (
                                <div dir="ltr">
                                    <span className={match.score1 > match.score2 ? 'text-white' : 'text-gray-400'}>{match.score1}</span>
                                    <span className="text-gray-600 mx-2">-</span>
                                    <span className={match.score2 > match.score1 ? 'text-white' : 'text-gray-400'}>{match.score2}</span>
                                </div>
                            ) : (
                                <span className="text-gray-600 text-sm">VS</span>
                            )}
                        </div>

                        <div className={`flex items-center gap-2 flex-1 justify-start ${t2.id === userTeamId ? 'text-pitch font-bold' : 'text-white'}`}>
                            <span className="text-2xl">{t2.flag}</span>
                            <span className="hidden sm:inline">{t2.name}</span>
                        </div>
                    </div>

                    <div className="w-full md:w-32 flex justify-end">
                        {match.isPlayed ? (
                            <div className="text-center w-full">
                                {match.isExtraTime && <div className="text-[10px] text-gray-400 mb-1">وقت إضافي</div>}
                                {match.penalty1 !== null && <div className="text-xs text-pitch font-bold" dir="ltr">({match.penalty1}) - ({match.penalty2})</div>}
                            </div>
                        ) : (
                            <button onClick={onAction} className={`w-full py-2 rounded-lg text-sm font-bold transition ${isUserMatch ? 'bg-pitch hover:bg-pitch-dark text-white' : 'bg-navy-700 hover:bg-navy-600 text-gray-300'}`}>
                                {isUserMatch ? 'إدخال النتيجة' : 'إدخال / محاكاة'}
                            </button>
                        )}
                    </div>
                </div>
            );
        };

        const MatchEntryModal = ({ match, teams, isUserMatch, onClose, onSave }) => {
            const t1 = teams.find(t => t.id === match.team1Id);
            const t2 = teams.find(t => t.id === match.team2Id);
            
            const [score1, setScore1] = useState(match.score1 !== null ? match.score1 : '');
            const [score2, setScore2] = useState(match.score2 !== null ? match.score2 : '');
            const [isExtraTime, setIsExtraTime] = useState(match.isExtraTime || false);
            const [penalty1, setPenalty1] = useState(match.penalty1 !== null ? match.penalty1 : '');
            const [penalty2, setPenalty2] = useState(match.penalty2 !== null ? match.penalty2 : '');

            const isKnockout = match.stage === 'knockout';
            const requiresPenalties = isKnockout && (parseInt(score1)||0) === (parseInt(score2)||0);

            return (
                <div className="fixed inset-0 bg-black/80 backdrop-blur-sm flex items-center justify-center z-50 p-4">
                    <div className="bg-navy-800 border border-gray-600 rounded-2xl p-6 md:p-8 w-full max-w-md shadow-2xl relative">
                        <button onClick={onClose} className="absolute top-4 left-4 text-gray-400 hover:text-white text-xl">
                            <i className="fas fa-times"></i>
                        </button>
                        
                        <h3 className="text-2xl font-bold text-center text-white mb-6">
                            {isUserMatch ? 'إدخال نتيجة مباراتك' : 'تسجيل نتيجة المباراة'}
                        </h3>

                        <div className="flex justify-between items-center mb-8">
                            <div className="text-center flex-1">
                                <div className="text-5xl mb-2">{t1.flag}</div>
                                <div className="font-bold text-lg text-white mb-2">{t1.name}</div>
                                <input type="number" min="0" value={score1} onChange={e=>setScore1(e.target.value)} className="w-20 text-center bg-navy-900 border border-gray-600 text-white text-2xl p-2 rounded-xl focus:border-pitch focus:outline-none" />
                            </div>
                            
                            <div className="text-2xl font-black text-gray-500 px-4">VS</div>
                            
                            <div className="text-center flex-1">
                                <div className="text-5xl mb-2">{t2.flag}</div>
                                <div className="font-bold text-lg text-white mb-2">{t2.name}</div>
                                <input type="number" min="0" value={score2} onChange={e=>setScore2(e.target.value)} className="w-20 text-center bg-navy-900 border border-gray-600 text-white text-2xl p-2 rounded-xl focus:border-pitch focus:outline-none" />
                            </div>
                        </div>

                        {isKnockout && (
                            <div className="mb-6 bg-navy-900 p-4 rounded-xl border border-gray-700">
                                <label className="flex items-center gap-2 text-gray-300 mb-3 cursor-pointer">
                                    <input type="checkbox" checked={isExtraTime} onChange={e=>setIsExtraTime(e.target.checked)} className="w-4 h-4 accent-pitch" />
                                    <span>هل امتدت المباراة إلى وقت إضافي؟</span>
                                </label>
                                
                                {requiresPenalties && (
                                    <div className="mt-4 pt-4 border-t border-gray-700 animate-fade-in">
                                        <p className="text-gold font-bold text-sm mb-3 text-center">نتيجة ركلات الترجيح (يجب تحديد فائز)</p>
                                        <div className="flex justify-center items-center gap-4">
                                            <input type="number" min="0" value={penalty1} onChange={e=>setPenalty1(e.target.value)} className="w-16 text-center bg-navy-800 border border-gold text-white text-xl p-1 rounded-lg" />
                                            <span className="text-gray-500">-</span>
                                            <input type="number" min="0" value={penalty2} onChange={e=>setPenalty2(e.target.value)} className="w-16 text-center bg-navy-800 border border-gold text-white text-xl p-1 rounded-lg" />
                                        </div>
                                    </div>
                                )}
                            </div>
                        )}

                        <button onClick={() => {
                            let s1 = score1 === '' ? 0 : parseInt(score1);
                            let s2 = score2 === '' ? 0 : parseInt(score2);
                            let p1 = penalty1 === '' ? 0 : parseInt(penalty1);
                            let p2 = penalty2 === '' ? 0 : parseInt(penalty2);

                            if (requiresPenalties && p1 === p2) {
                                alert("في الأدوار الإقصائية لا يمكن أن تنتهي ركلات الترجيح بالتعادل.");
                                return;
                            }
                            onSave({ score1: s1, score2: s2, isExtraTime, penalty1: requiresPenalties?p1:null, penalty2: requiresPenalties?p2:null });
                        }} className="w-full bg-pitch hover:bg-pitch-dark text-white font-bold py-3 rounded-xl transition text-lg shadow-lg">
                            تأكيد وحفظ النتيجة
                        </button>
                    </div>
                </div>
            );
        };

        const KnockoutView = ({ tournament }) => {
            if (!tournament) return null;

            const rounds = tournament.knockoutRounds || [];
            if (rounds.length === 0) return <div className="text-center text-gray-400 mt-20">لا توجد أدوار إقصائية مهيأة.</div>;

            const renderMatchBox = (match, t) => {
                if (!match) return <div className="w-40 md:w-48 h-16 bg-navy-900/50 border border-gray-700/50 rounded-lg"></div>;
                
                const t1 = match.team1Id ? t.teams.find(tm => tm.id === match.team1Id) : null;
                const t2 = match.team2Id ? t.teams.find(tm => tm.id === match.team2Id) : null;

                const isUser = (t1 && t1.id === t.userTeamId) || (t2 && t2.id === t.userTeamId);

                return (
                    <div className={`w-40 md:w-48 bg-navy-900 border rounded-lg overflow-hidden shadow-lg relative ${isUser ? 'border-pitch' : 'border-gray-700'}`}>
                        {match.isPlayed && match.penalty1 !== null && <div className="absolute top-0 left-0 text-[9px] bg-gold text-black px-1 rounded-br z-10">ترجيح</div>}
                        <div className="flex flex-col text-sm">
                            <div className="flex justify-between items-center p-2 border-b border-gray-800">
                                <div className="flex items-center gap-2 truncate">
                                    <span>{t1 ? t1.flag : '❔'}</span>
                                    <span className="truncate w-20">{t1 ? t1.name : 'يحدد لاحقاً'}</span>
                                </div>
                                <span className="font-bold text-white">{match.isPlayed ? match.score1 : '-'}</span>
                            </div>
                            <div className="flex justify-between items-center p-2 bg-navy-800/50">
                                <div className="flex items-center gap-2 truncate">
                                    <span>{t2 ? t2.flag : '❔'}</span>
                                    <span className="truncate w-20">{t2 ? t2.name : 'يحدد لاحقاً'}</span>
                                </div>
                                <span className="font-bold text-white">{match.isPlayed ? match.score2 : '-'}</span>
                            </div>
                        </div>
                    </div>
                );
            };

            const getRoundName = (r) => {
                switch(r) {
                    case 'R16': return 'دور الـ 16';
                    case 'QF': return 'ربع النهائي';
                    case 'SF': return 'نصف النهائي';
                    case 'F': return 'النهائي';
                    default: return r;
                }
            };

            return (
                <div className="animate-fade-in pb-20 overflow-x-auto custom-scrollbar">
                    <h2 className="text-3xl font-black text-white mb-8 border-r-4 border-pitch pr-4 sticky right-0">الأدوار الإقصائية</h2>
                    
                    <div className="flex min-w-max gap-8 px-4 relative">
                        {rounds.map((round, rIndex) => {
                            const roundMatches = tournament.fixtures.filter(f => f.round === round);
                            return (
                                <div key={round} className="flex flex-col gap-8 justify-around relative">
                                    <div className="text-center font-bold text-gold bg-navy-900 py-2 rounded-xl border border-gray-700 sticky top-0 z-10">
                                        {getRoundName(round)}
                                    </div>
                                    <div className="flex flex-col justify-around flex-1 gap-6 py-4">
                                        {roundMatches.map((m, i) => (
                                            <div key={m.id} className="relative z-10">
                                                {renderMatchBox(m, tournament)}
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
            );
        };

        const SettingsView = ({ tournament, deleteTournament }) => (
            <div className="max-w-3xl mx-auto space-y-6">
                <h2 className="text-3xl font-black text-white mb-8 border-r-4 border-pitch pr-4">الإعدادات والسجل</h2>
                
                <div className="glass-panel p-6 rounded-2xl">
                    <h3 className="text-xl font-bold text-white mb-4"><i className="fas fa-info-circle text-pitch ml-2"></i> معلومات البطولة</h3>
                    <ul className="space-y-3 text-gray-300">
                        <li><strong>الاسم:</strong> {tournament.info.name}</li>
                        <li><strong>النسخة:</strong> {tournament.info.edition} ({tournament.info.year})</li>
                        <li><strong>الاستضافة:</strong> {tournament.info.host}</li>
                        <li><strong>عدد المنتخبات:</strong> {tournament.teams.length}</li>
                        <li><strong>نمط المحاكاة:</strong> {tournament.format.simMode === 'realistic' ? 'واقعية' : tournament.format.simMode === 'surprises' ? 'مفاجآت' : 'متوازنة'}</li>
                    </ul>
                </div>

                <div className="glass-panel p-6 rounded-2xl border-t-4 border-red-500 mt-8">
                    <h3 className="text-xl font-bold text-white mb-4"><i className="fas fa-exclamation-triangle text-red-500 ml-2"></i> منطقة الخطر</h3>
                    <p className="text-gray-400 mb-6 text-sm">إجراءات لا يمكن التراجع عنها وتؤدي إلى مسح جميع البيانات المتعلقة بالبطولة الحالية.</p>
                    <button onClick={deleteTournament} className="bg-red-600 hover:bg-red-700 text-white font-bold py-3 px-6 rounded-xl transition flex items-center gap-2">
                        <i className="fas fa-trash-alt"></i> حذف البطولة الحالية بالكامل
                    </button>
                </div>
            </div>
        );

        const root = ReactDOM.createRoot(document.getElementById('root'));
        root.render(<App />);
    </script>
</body>
</html>
```