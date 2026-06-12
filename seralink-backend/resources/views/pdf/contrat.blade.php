<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: "Times New Roman", Times, serif;
            font-size: 12px;
            color: #1a1a1a;
            margin: 0;
            padding: 0;
        }
        .page {
            padding: 40px 50px;
        }
        .title-box {
            border: 2px solid #2E7D32;
            text-align: center;
            padding: 14px 20px;
            margin-bottom: 30px;
            background-color: #E8F5E9;
        }
        .title-box h1 {
            font-size: 18px;
            font-weight: bold;
            color: #2E7D32;
            margin: 0;
            text-transform: uppercase;
            letter-spacing: 2px;
        }
        .title-box p {
            font-size: 11px;
            color: #555;
            margin: 4px 0 0;
        }
        .section-label {
            color: #2E7D32;
            font-weight: bold;
            font-size: 13px;
            margin-top: 24px;
            margin-bottom: 8px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .party-block {
            margin-bottom: 20px;
            line-height: 1.9;
            padding: 12px 16px;
            background: #E8F5E9;
            border-left: 4px solid #2E7D32;
            border-radius: 4px;
        }
        .attendu {
            font-weight: bold;
            margin-bottom: 6px;
            line-height: 1.9;
        }
        .intro-italic {
            font-style: italic;
            margin: 16px 0;
            line-height: 1.9;
            padding: 10px 14px;
            border: 1px solid #2E7D32;
            border-radius: 4px;
            background: #f9fdf9;
        }
        .convenu {
            font-weight: bold;
            margin: 20px 0;
            text-transform: uppercase;
            text-align: center;
            font-size: 13px;
            color: #2E7D32;
            letter-spacing: 1px;
        }
        .article-title {
            color: #2E7D32;
            font-weight: bold;
            margin-top: 20px;
            margin-bottom: 6px;
            font-size: 13px;
        }
        .article-content {
            line-height: 1.9;
            margin-bottom: 10px;
        }
        ul {
            margin: 8px 0 8px 20px;
            line-height: 1.9;
        }
        ul li {
            margin-bottom: 4px;
        }
        .divider {
            border: none;
            border-top: 2px solid #2E7D32;
            margin: 20px 0;
            opacity: 0.3;
        }
        .signature-section {
            margin-top: 40px;
        }
        .signature-table {
            width: 100%;
            border-collapse: collapse;
        }
        .signature-table td {
            width: 50%;
            padding: 10px 20px;
            vertical-align: top;
            text-align: center;
        }
        .signature-label {
            font-weight: bold;
            margin-bottom: 10px;
            font-size: 13px;
            color: #2E7D32;
            text-transform: uppercase;
        }
        .signed-box {
            margin-top: 12px;
            background: #E8F5E9;
            border: 1px solid #2E7D32;
            border-radius: 6px;
            padding: 10px;
        }
        .signed-box .check {
            color: #2E7D32;
            font-weight: bold;
            font-size: 12px;
        }
        .signed-box .date {
            font-size: 10px;
            color: #555;
            margin-top: 4px;
        }
        .pending-box {
            margin-top: 12px;
            background: #FFF8E1;
            border: 1px solid #F9A825;
            border-radius: 6px;
            padding: 10px;
        }
        .pending-box .wait {
            color: #F57F17;
            font-weight: bold;
            font-size: 11px;
        }
        .footer {
            margin-top: 40px;
            border-top: 2px solid #2E7D32;
            padding-top: 10px;
            text-align: center;
            font-size: 10px;
            color: #2E7D32;
        }
        .ref-box {
            background: #E8F5E9;
            border: 1px solid #2E7D32;
            padding: 10px 16px;
            margin: 16px 0;
            font-size: 11px;
            color: #333;
            border-radius: 4px;
        }
        .amount-highlight {
            font-weight: bold;
            font-size: 20px;
            color: #2E7D32;
        }
        .amount-box {
            text-align: center;
            margin: 16px 0;
            padding: 14px;
            background: #E8F5E9;
            border: 2px solid #2E7D32;
            border-radius: 8px;
        }
        .page-break {
            page-break-after: always;
        }
        .header-logo {
            text-align: center;
            color: #2E7D32;
            font-size: 22px;
            font-weight: bold;
            letter-spacing: 3px;
            margin-bottom: 6px;
        }
        .header-sub {
            text-align: center;
            font-size: 11px;
            color: #555;
            margin-bottom: 20px;
        }
        .contract-number {
            text-align: right;
            font-size: 11px;
            color: #888;
            margin-bottom: 10px;
        }
        .info-line {
            line-height: 1.9;
        }
        .status-active {
            color: #2E7D32;
            font-weight: bold;
        }
        .status-pending {
            color: #F57F17;
            font-weight: bold;
        }
        .status-completed {
            color: #1565C0;
            font-weight: bold;
        }
        .status-disputed {
            color: #C62828;
            font-weight: bold;
        }
    </style>
</head>
<body>

<!-- PAGE 1 : PARTIES ET PRÉAMBULE -->
<div class="page">

    <div class="header-logo">🔗 SERALINK</div>
    <div class="header-sub">Plateforme Freelance Madagascar</div>
    <div class="contract-number">Contrat N° #{{ $contract->id }} — {{ \Carbon\Carbon::parse($contract->created_at)->format('d/m/Y') }}</div>

    <div class="title-box">
        <h1>Contrat de Prestation Freelance</h1>
        <p>{{ $contract->jobListing->title ?? 'Mission #'.$contract->job_listing_id }}</p>
    </div>

    <!-- PARTIE CLIENT -->
    <div class="section-label">Entre d'une part,</div>
    <div class="party-block">
        <strong>{{ strtoupper($contract->client->name ?? 'LE CLIENT') }}</strong>
        — ci-après dénommé(e) <strong>« Le Client »</strong><br>
        Email : {{ $contract->client->email ?? '—' }}<br>
        Rôle : Client SeraLink
    </div>

    <!-- PARTIE FREELANCE -->
    <div class="section-label">Et d'autre part ;</div>
    <div class="party-block">
        <strong>{{ strtoupper($contract->freelance->name ?? 'LE FREELANCE') }}</strong>
        — ci-après dénommé(e) <strong>« Le Freelancer »</strong><br>
        Email : {{ $contract->freelance->email ?? '—' }}<br>
        Rôle : Freelance SeraLink
    </div>

    <div class="divider"></div>

    <!-- PRÉAMBULE -->
    <div class="attendu">
        Attendu que le Client a besoin d'un(e) prestataire en {{ $contract->jobListing->category ?? 'services freelance' }} ;<br>
        Attendu que le Freelancer dispose des compétences requises et exprime son intérêt à exécuter ces services ;<br>
        Attendu que les parties souhaitent établir les termes et conditions selon lesquels ces services seront fournis ;
    </div>

    <div class="intro-italic">
        Ce contrat décrit les modalités de travail entre
        <strong>{{ strtoupper($contract->client->name ?? 'LE CLIENT') }}</strong> et
        <strong>{{ strtoupper($contract->freelance->name ?? 'LE FREELANCE') }}</strong>
        pour la mission : <strong>« {{ $contract->jobListing->title ?? 'Mission #'.$contract->job_listing_id }} »</strong>,
        à compter du {{ \Carbon\Carbon::parse($contract->created_at)->format('d/m/Y') }}.
    </div>

    <div class="convenu">— Il a été arrêté et convenu ce qui suit —</div>

    <!-- ARTICLE 1 -->
    <div class="article-title">Article 1 : Objet du Contrat</div>
    <div class="article-content">
        Le présent contrat a pour objet de définir les conditions et modalités de travail entre
        <strong>{{ strtoupper($contract->client->name ?? 'LE CLIENT') }}</strong> et
        <strong>{{ strtoupper($contract->freelance->name ?? 'LE FREELANCE') }}</strong>.
    </div>
    <div class="article-content">
        <strong>Mission :</strong> {{ $contract->jobListing->title ?? 'Mission #'.$contract->job_listing_id }}<br>
        <strong>Catégorie :</strong> {{ $contract->jobListing->category ?? '—' }}
    </div>
    @if($contract->jobListing && $contract->jobListing->description)
    <div class="article-content">
        <strong>Description :</strong> {{ $contract->jobListing->description }}
    </div>
    @endif

    <div class="footer">
        Page 1/3 — SeraLink © {{ now()->year }} — Plateforme Freelance Madagascar
    </div>
</div>

<!-- PAGE 2 : CONDITIONS FINANCIÈRES -->
<div class="page-break"></div>
<div class="page">

    <div class="header-logo">🔗 SERALINK</div>
    <div class="header-sub">Plateforme Freelance Madagascar</div>
    <div class="contract-number">Contrat N° #{{ $contract->id }} — Conditions Financières</div>

    <div class="title-box">
        <h1>Conditions Financières & Obligations</h1>
    </div>

    <!-- ARTICLE 2 -->
    <div class="article-title">Article 2 : Rémunération et Paiement Escrow</div>
    <div class="article-content">
        En contrepartie des services rendus, le Client s'engage à verser au Freelancer :
    </div>
    <div class="amount-box">
        <span class="amount-highlight">{{ number_format($contract->amount, 0, ',', ' ') }} Ariary (MGA)</span>
    </div>
    <div class="article-content">
        Le paiement est sécurisé via le système <strong>Escrow Stripe</strong> intégré à SeraLink :
        <ul>
            <li>Le Client dépose les fonds sur le compte escrow lors de la signature.</li>
            <li>Les fonds sont retenus par SeraLink jusqu'à validation du travail.</li>
            <li>Après validation par le Client, les fonds sont libérés au Freelancer.</li>
            <li>En cas de litige, SeraLink intervient comme médiateur.</li>
        </ul>
    </div>

    <div class="ref-box">
        <strong>Statut paiement :</strong>
        @if($contract->payment_status == 'unpaid') <span style="color:#C62828;">Non payé</span>
        @elseif($contract->payment_status == 'escrowed') <span style="color:#1565C0;">En escrow 🔒 (fonds retenus)</span>
        @elseif($contract->payment_status == 'released') <span style="color:#2E7D32;">Payé et libéré ✓</span>
        @else {{ $contract->payment_status }}
        @endif
        @if($contract->stripe_payment_id)
        <br><strong>Référence Stripe :</strong> {{ $contract->stripe_payment_id }}
        @endif
    </div>

    <!-- ARTICLE 3 -->
    <div class="article-title">Article 3 : Délai d'Exécution</div>
    <div class="article-content">
        Le Freelancer s'engage à livrer le travail au plus tard le :
        <strong>
            @if($contract->deadline)
                {{ \Carbon\Carbon::parse($contract->deadline)->format('d/m/Y') }}
            @else
                À convenir entre les parties
            @endif
        </strong>
    </div>

    <!-- ARTICLE 4 -->
    <div class="article-title">Article 4 : Obligations du Freelancer</div>
    <div class="article-content">
        <ul>
            <li>Exécuter la mission conformément aux spécifications définies ;</li>
            <li>Respecter les délais convenus ;</li>
            <li>Communiquer régulièrement l'avancement via la messagerie SeraLink ;</li>
            <li>Livrer un travail de qualité professionnelle ;</li>
            <li>Respecter la confidentialité des informations du Client ;</li>
            <li>Signaler tout obstacle dans les meilleurs délais.</li>
        </ul>
    </div>

    <!-- ARTICLE 5 -->
    <div class="article-title">Article 5 : Obligations du Client</div>
    <div class="article-content">
        <ul>
            <li>Fournir toutes les informations nécessaires à l'exécution de la mission ;</li>
            <li>Déposer les fonds escrow dans les 48h suivant la signature ;</li>
            <li>Valider ou refuser le travail dans un délai de 7 jours après livraison ;</li>
            <li>Communiquer clairement ses besoins et retours ;</li>
            <li>Respecter les droits du Freelancer sur son travail.</li>
        </ul>
    </div>

    <div class="footer">
        Page 2/3 — SeraLink © {{ now()->year }} — Plateforme Freelance Madagascar
    </div>
</div>

<!-- PAGE 3 : CLAUSES FINALES ET SIGNATURES -->
<div class="page-break"></div>
<div class="page">

    <div class="header-logo">🔗 SERALINK</div>
    <div class="header-sub">Plateforme Freelance Madagascar</div>
    <div class="contract-number">Contrat N° #{{ $contract->id }} — Clauses Finales & Signatures</div>

    <div class="title-box">
        <h1>Clauses Finales & Signatures Électroniques</h1>
    </div>

    <!-- ARTICLE 6 -->
    <div class="article-title">Article 6 : Propriété Intellectuelle</div>
    <div class="article-content">
        À compter du paiement intégral, le Client acquiert les droits d'utilisation sur les livrables. Le Freelancer conserve le droit de mentionner cette réalisation dans son portfolio, sauf accord contraire explicite.
    </div>

    <!-- ARTICLE 7 -->
    <div class="article-title">Article 7 : Confidentialité</div>
    <div class="article-content">
        Les deux parties s'engagent à garder confidentielles toutes les informations échangées et à ne pas les divulguer à des tiers sans accord préalable écrit.
    </div>

    <!-- ARTICLE 8 -->
    <div class="article-title">Article 8 : Résiliation</div>
    <div class="article-content">
        En cas de non-respect des obligations, l'autre partie peut demander la résiliation via SeraLink :
        <ul>
            <li>Faute du Freelancer : remboursement intégral au Client.</li>
            <li>Faute du Client : paiement partiel au Freelancer selon le travail réalisé.</li>
        </ul>
    </div>

    <!-- ARTICLE 9 -->
    <div class="article-title">Article 9 : Règlement des Litiges</div>
    <div class="article-content">
        En cas de litige, les parties recherchent une solution amiable. SeraLink peut intervenir comme médiateur. À défaut, le litige est soumis aux juridictions compétentes de Madagascar.
    </div>

    <div class="divider"></div>

    <!-- RÉCAPITULATIF -->
    <div class="article-title">Récapitulatif du Contrat</div>
    <div class="ref-box info-line">
        <strong>N° Contrat :</strong> #{{ $contract->id }}<br>
        <strong>Date création :</strong> {{ \Carbon\Carbon::parse($contract->created_at)->format('d/m/Y à H:i') }}<br>
        <strong>Statut :</strong>
        @if($contract->status == 'active') <span class="status-active">Actif</span>
        @elseif($contract->status == 'pending_signatures') <span class="status-pending">En attente de signatures</span>
        @elseif($contract->status == 'completed') <span class="status-completed">Terminé</span>
        @elseif($contract->status == 'disputed') <span class="status-disputed">Litige</span>
        @else {{ $contract->status }}
        @endif
        <br>
        <strong>Montant :</strong> {{ number_format($contract->amount, 0, ',', ' ') }} Ariary (MGA)<br>
        <strong>Plateforme :</strong> SeraLink — Plateforme Freelance Madagascar
    </div>

    <div class="divider"></div>

    <!-- SIGNATURES -->
    <div class="signature-section">
        <div style="text-align:center; font-weight:bold; margin-bottom:20px; font-size:13px; color:#2E7D32;">
            Fait à Madagascar, le {{ now()->format('d/m/Y') }}
        </div>
        <table class="signature-table">
            <tr>
                <td>
                    <div class="signature-label">Le Client</div>
                    <div><strong>{{ strtoupper($contract->client->name ?? '—') }}</strong></div>
                    @if($contract->client_signed)
                    <div class="signed-box">
                        <div class="check">✓ Signé électroniquement</div>
                        <div class="date">
                            Le {{ \Carbon\Carbon::parse($contract->client_signed_at)->format('d/m/Y à H:i') }}<br>
                            Via SeraLink — Juridiquement valide
                        </div>
                    </div>
                    @else
                    <div class="pending-box">
                        <div class="wait">⏳ En attente de signature</div>
                    </div>
                    @endif
                </td>
                <td>
                    <div class="signature-label">Le Freelancer</div>
                    <div><strong>{{ strtoupper($contract->freelance->name ?? '—') }}</strong></div>
                    @if($contract->freelance_signed)
                    <div class="signed-box">
                        <div class="check">✓ Signé électroniquement</div>
                        <div class="date">
                            Le {{ \Carbon\Carbon::parse($contract->freelance_signed_at)->format('d/m/Y à H:i') }}<br>
                            Via SeraLink — Juridiquement valide
                        </div>
                    </div>
                    @else
                    <div class="pending-box">
                        <div class="wait">⏳ En attente de signature</div>
                    </div>
                    @endif
                </td>
            </tr>
        </table>
    </div>

    <div class="footer">
        Page 3/3 — Contrat généré le {{ now()->format('d/m/Y à H:i') }} —
        SeraLink © {{ now()->year }} — Plateforme Freelance Madagascar —
        Ce document constitue un accord juridiquement contraignant.
    </div>

</div>

</body>
</html>