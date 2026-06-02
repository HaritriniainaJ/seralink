<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Proposal;
use App\Models\Contract;
use App\Models\JobListing;
use Illuminate\Http\Request;

class ProposalController extends Controller
{
    // Liste des propositions d'une mission
    public function index($jobId)
    {
        $proposals = Proposal::with('freelance:id,name,avatar')
            ->where('job_listing_id', $jobId)
            ->get();

        return response()->json($proposals);
    }

    // Soumettre une proposition (freelance)
    public function store(Request $request, $jobId)
    {
        $request->validate([
            'cover_letter' => 'required|string',
            'budget'       => 'required|integer',
            'deadline'     => 'required|date',
        ]);

        // Vérifier qu'il n'a pas déjà soumis
        $exists = Proposal::where('job_listing_id', $jobId)
            ->where('freelance_id', $request->user()->id)
            ->exists();

        if ($exists) {
            return response()->json(['message' => 'Vous avez déjà soumis une proposition'], 422);
        }

        $proposal = Proposal::create([
            'job_listing_id' => $jobId,
            'freelance_id'   => $request->user()->id,
            'cover_letter'   => $request->cover_letter,
            'budget'         => $request->budget,
            'deadline'       => $request->deadline,
            'status'         => 'pending',
        ]);

        return response()->json($proposal, 201);
    }

    // Accepter une proposition (client) → crée un contrat
    public function accept(Request $request, $id)
    {
        $proposal = Proposal::with('jobListing')->findOrFail($id);

        // Vérifier que c'est bien le client de la mission
        if ($proposal->jobListing->client_id !== $request->user()->id) {
            return response()->json(['message' => 'Non autorisé'], 403);
        }

        // Mettre à jour le statut
        $proposal->update(['status' => 'accepted']);

        // Refuser les autres propositions
        Proposal::where('job_listing_id', $proposal->job_listing_id)
            ->where('id', '!=', $proposal->id)
            ->update(['status' => 'rejected']);

        // Fermer la mission
        $proposal->jobListing->update(['status' => 'closed']);

        // Créer le contrat
        $contract = Contract::create([
            'job_listing_id' => $proposal->job_listing_id,
            'client_id'      => $request->user()->id,
            'freelance_id'   => $proposal->freelance_id,
            'amount'         => $proposal->budget,
            'status'         => 'active',
            'payment_status' => 'unpaid',
            'deadline'       => $proposal->deadline,
        ]);

        return response()->json([
            'message'  => 'Proposition acceptée',
            'contract' => $contract
        ]);
    }

    // Refuser une proposition (client)
    public function reject(Request $request, $id)
    {
        $proposal = Proposal::with('jobListing')->findOrFail($id);

        if ($proposal->jobListing->client_id !== $request->user()->id) {
            return response()->json(['message' => 'Non autorisé'], 403);
        }

        $proposal->update(['status' => 'rejected']);

        return response()->json(['message' => 'Proposition refusée']);
    }

    // Mes propositions (freelance)
    public function myProposals(Request $request)
    {
        $proposals = Proposal::with('jobListing:id,title,budget_min,budget_max,status')
            ->where('freelance_id', $request->user()->id)
            ->get();

        return response()->json($proposals);
    }
}