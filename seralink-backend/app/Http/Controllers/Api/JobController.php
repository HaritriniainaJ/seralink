<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\JobListing;
use Illuminate\Http\Request;

class JobController extends Controller
{
    // Liste des missions avec filtres
    public function index(Request $request)
    {
        $query = JobListing::with('client:id,name,avatar')
            ->where('status', 'open');

        if ($request->category) {
            $query->where('category', $request->category);
        }

        if ($request->budget_min) {
            $query->where('budget_min', '>=', $request->budget_min);
        }

        if ($request->budget_max) {
            $query->where('budget_max', '<=', $request->budget_max);
        }

        if ($request->search) {
            $query->where('title', 'like', '%' . $request->search . '%');
        }

        $jobs = $query->orderBy('created_at', 'desc')->get();
        return response()->json(['data' => $jobs, 'total' => $jobs->count()]);
    }

    // Détail d'une mission
    public function show($id)
    {
        $job = JobListing::with('client:id,name,avatar')->findOrFail($id);
        return response()->json($job);
    }

    // Créer une mission (client)
    public function store(Request $request)
    {
        $request->validate([
            'title'       => 'required|string|max:255',
            'description' => 'required|string',
            'category'    => 'required|string',
            'budget_min'  => 'required|integer',
            'budget_max'  => 'required|integer',
            'budget_type' => 'required|in:fixed,hourly',
            'deadline'    => 'required|date',
        ]);

        $job = JobListing::create([
            'client_id'   => $request->user()->id,
            'title'       => $request->title,
            'description' => $request->description,
            'category'    => $request->category,
            'budget_min'  => $request->budget_min,
            'budget_max'  => $request->budget_max,
            'budget_type' => $request->budget_type,
            'deadline'    => $request->deadline,
            'status'      => 'open',
        ]);

        return response()->json($job, 201);
    }

    // Modifier une mission (client)
    public function update(Request $request, $id)
    {
        $job = JobListing::where('client_id', $request->user()->id)
            ->findOrFail($id);

        $job->update($request->only([
            'title', 'description', 'category',
            'budget_min', 'budget_max', 'budget_type', 'deadline', 'status'
        ]));

        return response()->json($job);
    }

    // Supprimer une mission (client)
    public function destroy(Request $request, $id)
    {
        $job = JobListing::where('client_id', $request->user()->id)
            ->findOrFail($id);

        $job->delete();

        return response()->json(['message' => 'Mission supprimée']);
    }
}