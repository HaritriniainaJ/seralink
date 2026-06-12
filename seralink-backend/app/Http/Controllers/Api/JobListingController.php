<?php
namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\JobListing;
use Illuminate\Http\Request;

class JobListingController extends Controller
{
    public function index(Request $request)
    {
        $query = JobListing::with('client:id,name,avatar')
            ->where('status', 'open');

        if ($request->filled('category')) {
            $query->where('category', $request->category);
        }

        if ($request->filled('budget_min')) {
            $query->where('budget_max', '>=', $request->budget_min);
        }

        if ($request->filled('budget_max')) {
            $query->where('budget_min', '<=', $request->budget_max);
        }

        if ($request->filled('search')) {
            $query->where(function ($q) use ($request) {
                $q->where('title', 'like', '%'.$request->search.'%')
                  ->orWhere('description', 'like', '%'.$request->search.'%');
            });
        }

        $jobs = $query->orderBy('created_at', 'desc')->get();

        return response()->json(['data' => $jobs, 'total' => $jobs->count()]);
    }
}