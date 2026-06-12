<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Contract;
use App\Models\JobListing;
use App\Models\Proposal;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    // Dashboard client
public function client(Request $request)
{
    $userId = $request->user()->id;

    $missions = JobListing::where('client_id', $userId)
        ->withCount('proposals')
        ->orderBy('created_at', 'desc')
        ->get();

    $contracts = Contract::with('freelance:id,name,avatar')
        ->where('client_id', $userId)
        ->orderBy('created_at', 'desc')
        ->get();

    $recentProposals = \App\Models\Proposal::with([
        'freelance:id,name,avatar',
        'jobListing:id,title,category'
    ])
    ->whereHas('jobListing', fn($q) => $q->where('client_id', $userId))
    ->orderBy('created_at', 'desc')
    ->limit(10)
    ->get()
    ->map(function ($p) {
        $p->freelancer = $p->freelance;
        $p->job = $p->jobListing;
        return $p;
    });

    $stats = [
        'total_missions'   => $missions->count(),
        'open_missions'    => $missions->where('status', 'open')->count(),
        'active_contracts' => $contracts->where('status', 'active')->count(),
        'total_spent'      => $contracts->where('payment_status', 'paid')->sum('amount'),
        'total_proposals'  => $recentProposals->count(),
    ];

    return response()->json([
        'stats'            => $stats,
        'missions'         => $missions,
        'contracts'        => $contracts,
        'recent_proposals' => $recentProposals,
    ]);
}

    // Dashboard freelance
    public function freelance(Request $request)
    {
        $userId = $request->user()->id;

        $proposals = Proposal::with('jobListing:id,title,budget_min,budget_max,status')
            ->where('freelance_id', $userId)
            ->orderBy('created_at', 'desc')
            ->get();

        $contracts = Contract::with('client:id,name,avatar')
            ->where('freelance_id', $userId)
            ->orderBy('created_at', 'desc')
            ->get();

        $stats = [
            'total_proposals'  => $proposals->count(),
            'pending_proposals' => $proposals->where('status', 'pending')->count(),
            'active_contracts' => $contracts->where('status', 'active')->count(),
            'total_revenue'    => $contracts->where('payment_status', 'paid')->sum('amount'),
        ];

        return response()->json([
            'stats'     => $stats,
            'proposals' => $proposals,
            'contracts' => $contracts,
        ]);
    }
}